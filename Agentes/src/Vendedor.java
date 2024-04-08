import jade.core.Agent;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Vendedor extends Agent{
    private VendedorGUI gui;
    private HashMap<String, Subasta> subastas;
    private ArrayList<AID> compradores;
    private ArrayList<AID> compradoresAnteriores;
    private ArrayList<RondaSubasta> rondasSubasta;
    private ArrayList<RondaSubasta> subastasAEliminar;

    public Vendedor(){
        this.gui = new VendedorGUI(this);
        this.subastas = new HashMap<>();
        this.rondasSubasta = new ArrayList<>();
        this.compradores = new ArrayList<>();
        this.compradoresAnteriores = new ArrayList<>();
        this.subastasAEliminar = new ArrayList<>();
    }
    
    @Override
    protected void setup(){

        // Registramos el servidor en el servicio de paginas amarillas
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        dfd.addServices(sd);
        sd.setType("Vendedor");
        sd.setName("JADE-book-Vendedor");
        dfd.addServices(sd);
        try {
                DFService.register(this, dfd);
                System.out.println("Vendedor " + this.getAID().getName() + " registrado");
        }
        catch (FIPAException fe) {
                System.out.println("Error al registrarse en el DFService" + fe.getMessage());
        }
        
        // Añadimos un TickerBehaviour que compruebe si hay nuevos clientes en la puja
        addBehaviour(new TickerBehaviour(this, 10000){ 
            @Override
            protected void onTick(){
                // Comprobamos si hay subastas activas y realizamos el proceso de subasta
                if (subastas.keySet().size() > 0){
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("Comprador");
                    template.addServices(sd);
                    try {
                        // Buscamos en el servicio de páginas amarillas por los compradores
                        DFAgentDescription[] result = DFService.search(myAgent, template); 
                        // Reseteamos los compradores anteriores
                        compradoresAnteriores.clear();
                        for(AID comprador: compradores){
                            compradoresAnteriores.add(comprador);
                        }
                        // Reseteamos los compradores
                        compradores.clear();
                        
                        System.out.println("----------------------------");
                        System.out.println("Vendedor: Se han encontrado " + result.length + " compradores");
                        if (result.length > 0){
                            System.out.println("Los siguientes compradores han sido encontrados:");
                            for (int i = 0; i < result.length; ++i) {
                                compradores.add(result[i].getName());
                                System.out.println(result[i].getName());
                            }
                        }
                        
                    }
                    catch (FIPAException fe) {
                        fe.printStackTrace();
                    }
                    
                    // Actualizamos los precios en la ronda de la subasta
                    Iterator<RondaSubasta> iterator = rondasSubasta.iterator();
                    while (iterator.hasNext()) {
                        RondaSubasta subasta = iterator.next();
                        // Si la subasta ya se termino, se elimina de la lista de subastas
                        if (subasta.getSubastaTerminada()) {
                            subastas.remove(subasta.libro);
                            iterator.remove();  // Use iterator.remove() to safely remove the current element
                        }
                        else{
                            System.out.println("Cantidad de subastas: " + rondasSubasta.size());
                            actualizarTabla(subasta.libro, subasta.precio,  "Activa", subasta.comprador);
                            addBehaviour(subasta);
                        }
                    }
                    System.out.println("Cantidad de libros en subasta: " + subastas.keySet().size());
                    System.out.println();
                }
            }
            
        });
        
    }
    
    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
            System.out.println("Vendedor " + this.getAID().getName() + " desregistrado");
        } catch (Exception e) {
            System.out.println("Error al desregistrarse en el DFService" + e.getMessage());
        }
    }
    
    
    private class RondaSubasta extends Behaviour {

        private String libro;
        private boolean subastaTerminada = false;
        private boolean rondaTerminada = false;
        private int tiempoTranscurrido = 0;
        private int step = 0;
        private MessageTemplate mt; // Plantilla para los mensajes
        private AID comprador = null;
        private AID compradorAnterior = null;
        private Float precio;
        private Float incremento = 10.0f;
        private Integer tiempoSinPujas = 0;
        private Float precioRondaAnterior = 0.0f;

        public RondaSubasta(String libro, Float precio) {
            this.libro = libro;
            this.subastaTerminada = false;
            this.precio = precio;
        }
        
        @Override
        public void action(){
            this.rondaTerminada = false;
            System.out.println("Action - Actualizando precio del libro " + this.libro);
            // Comprobamos que el libro se esté subastando
            if (!(subastas.containsKey(this.libro))){
                return;
            }
            // Obtenemos la subasta
            Subasta subasta = subastas.get(this.libro);
            
            // Si no hay ningun comprador se sale del action
            if (subasta.getComprador() == this.hashCode()){
                return;
            }
            System.out.println("-- ¿Alguien da " + this.precio + "€? --");
            
            switch (step) {
                case 0: 
                    // Mandar CFP a todos los compradores
                    // System.out.println("Paso 0 - Compradores: " + compradores.size());
                    ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                    if (!compradores.isEmpty()){
                        for (AID comprador : compradores) {
                            cfp.addReceiver(comprador);
                        } 
                        cfp.setContent(this.libro + "," + this.precio);
                        cfp.setConversationId("book-trade");
                        cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Valor único
                        myAgent.send(cfp);
                        // Preparamos la plantailla para los CFP
                        mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
                                MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                        this.step = 1;
                    }
                    else{
                        System.out.println("No hay todavia compradores para " + this.libro);
                        return;
                    }
                    break;
                case 1:
                    // Miramos si hay algun comprador interesado
                    // System.out.println("Paso 1");
                    ACLMessage reply = myAgent.receive(mt);
                    if (reply != null) {
                        // Respuesta recibida
                        System.out.println("Propuesta recibida en el vendedor");
                        // Si la respuesta es una propuesta
                        if (reply.getPerformative() == ACLMessage.PROPOSE){
                            // Si es la primera iteracion, el comprador sera null
                            if (this.comprador == null){
                                this.comprador = reply.getSender();
                            }
                            else{
                                // Si ya hay un comprador, se compara con el anterior
                                this.compradorAnterior = this.comprador;
                                this.comprador = reply.getSender();
                            }

                            if (this.compradorAnterior != null && this.comprador.getName().equals(this.compradorAnterior.getName())){
                                System.out.println("-- MISMO COMPRADOR --");
                            }
                            else{
                                System.out.println("PROPOSE CORRECTA - AUMENTANDO PRECIO");
                                tiempoSinPujas = 0;
                                // Hay algun comprador interesado
                                // Se actualiza el precio y el comprador
                                actualizarTabla(this.libro, this.precio,  null, this.comprador);
                                this.precio += this.incremento;
                                this.comprador = reply.getSender();
                                // Indicamos que hay al menos un comprador interesado
                                subasta.setComprador(this.comprador.hashCode());
                                this.step = 0;
                                this.rondaTerminada = true;
                            }
                            return;
                            
                        }
                        else if(reply.getPerformative() == ACLMessage.REFUSE){
                            System.out.println("REFUSE RECIBIDO");
                        }
                    }
                    else {
                        System.out.println("No se han recibido ofertas");
                        try{
                            Thread.sleep(100);
                        }catch(InterruptedException e){
                            System.out.println("Error al dormir el hilo");
                        }
                        
                        tiempoSinPujas++;
                        System.out.println("T: " + tiempoSinPujas);

                        // Si no hay pujas durante 3 segundos se vuelve a contactar
                        // con el servicio de paginas amarillas y se vuelve a mandar
                        // el CFP a todos los compradores
                        if (tiempoSinPujas > 3){
                            step = 0;
                            // Si hay algun comprador terminamos la subasta
                            this.rondaTerminada = true;
                            if(this.comprador != null){
                                this.precio -= this.incremento;
                                actualizarTabla(this.libro, this.precio,  "Finalizada", this.comprador.getName());
                                System.out.println("Han pasado 3 segundos y no hay pujas");
                                System.out.println("\n================================================");
                                System.out.println("----------------- FIN SUBASTA -----------------");
                                System.out.println("================================================");
                                System.out.println("Subasta de " + this.libro + " finalizada");
                                System.out.println("Comprador: " + this.comprador.getName());
                                System.out.println("Precio final: " + this.precio);
                                System.out.println("Notificando al ganador...");
                                System.out.println("================================================\n");
                                this.subastaTerminada = true;
                                
                                // Notificamos al ganador de que ha ganado
                                ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
                                inform.addReceiver(this.comprador);
                                inform.setContent(this.libro + "," + this.precio);
                                inform.setConversationId("book-trade");
                                inform.setReplyWith("cfp" + System.currentTimeMillis()); // Valor único
                                myAgent.send(inform);
                            }
                            // Si no hay ningun comprador, esperamos a la siguiente iteracion
                            else{
                                tiempoSinPujas = 0;
                                System.out.println("No hay compradores para esta ronda del libro: " + this.libro);
                            }
                            return;
                        }
                    }
                    break;
                
            }
        }
        
        @Override
        public boolean done() {
            System.out.println("\n");
            return ((this.step == 0 || this.rondaTerminada == true || this.subastaTerminada == true));
        }

        public boolean getSubastaTerminada() {
            return this.subastaTerminada;
        }
    }
    
    
    
    
    public class Subasta extends Agent{
        private Float precio;
        private Integer comprador = 0;
        
        public Subasta(Float precio, Integer comprador){
            this.precio = precio;
            this.comprador = comprador;
        }

        public Float getPrecio() {
            return precio;
        }

        public Integer getComprador() {
            return comprador;
        }

        public void setPrecio(Float precio) {
            this.precio = precio;
        }

        public void setComprador(Integer comprador) {
            this.comprador = comprador;
        }        
    }

    public void anadirLibroSubasta(Float precio, String libro) {
        Subasta subasta = new Subasta (precio, this.hashCode());
        if (!(this.subastas.containsKey(libro))){
            this.subastas.put(libro, subasta);
            this.rondasSubasta.add(new RondaSubasta(libro, precio));
        }
    }
    
    private void actualizarTabla(String titulo, Object Precio, Object estado, Object ganador) {
            // Buscamos la fila de la tabla que contenga dicho libro:
                // Recorre las filas de la tabla
                for (int i = 0; i < gui.getTblModel().getRowCount(); i++) {
                    // Obtiene el valor en la primera columna de la fila actual
                    String valorEnPrimeraColumna = (String) gui.getTblModel().getValueAt(i, 0);

                    // Compara el valor con el String buscado
                    if (valorEnPrimeraColumna.equals(titulo)) {
                        // Si es igual, imprime el número de fila (puedes hacer lo que necesites con el índice)
                        int numeroDeFila = i;
                        if (Precio != null){
                            gui.getTblModel().setValueAt(Precio, numeroDeFila, 1);
                        }
                        if (estado != null){
                            gui.getTblModel().setValueAt(estado, numeroDeFila, 2);
                        }
                        if (ganador != null){
                            gui.getTblModel().setValueAt(ganador, numeroDeFila, 3);
                        }
                            
                        // Datos actualizados
                        break; 
                    }
                }
        }
    
    
    
}
