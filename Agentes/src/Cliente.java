import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente extends Agent{
    
    private ClienteGUI gui = null;
    
    
    public Cliente(){
        this.gui = new ClienteGUI(this);
        
    }
    
    public static void main(String[] args){
        Cliente cliente = new Cliente();
    }
    
    @Override
    protected void setup(){
        System.out.println("Entrando comprador " + this.getAID().getName());
        
        // El cliente se registra en el servicio de paginas amarillas
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(this.getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Comprador");
        sd.setName("JADE-book-auction");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            System.out.println("Error al registrarse en el DFService" + fe.getMessage());
        }
        
        System.out.println("Comprador " + this.getAID().getName() + " registrado");
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        addBehaviour(new Pujar());
        
    }
    
    private class Pujar extends CyclicBehaviour{
        @Override
        public void action() {
            // Comprobamos si hay mensajes CFP
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg == null) {
                // Comprobamos hay algun mensaje que nos informe que hemos ganado la subasta
                mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                msg = myAgent.receive(mt);
                if (msg == null) {
                    // Si no hay mensajes, se bloquea el comportamiento
                    block();
                    return;
                }
                else{
                    // Se ha recibido un mensaje de fin de subasta
                    String mensaje = msg.getContent();
                    // Dividir el string en base a la coma
                    String[] partes = mensaje.split(",");
                    // Obtener el título y el precio del mensaje
                    String titulo = partes[0];
                    Float precio = Float.parseFloat(partes[1]);
                    // Actualizamos la GUI
                    actualizarTabla(titulo, precio,  "Ganador");
                    // Eliminamos el libro buscado
                    gui.getLibros().remove(titulo);
                    
                    return;
                }
            }
            // CFP recibido, se pasa a su procesado
            String mensaje = msg.getContent();
            ACLMessage reply = msg.createReply();

            // Dividir el string en base a la coma
            String[] partes = mensaje.split(",");
            // Obtener el título y el precio del mensaje
            String titulo = partes[0];
            Float precio = Float.parseFloat(partes[1]);
            Float price = getGui().getLibros().get(titulo);

            // Si el libro está en la lista de libros del comprador se puja por él
            // Y si el precio es menor o igual al precio que el comprador está dispuesto a pagar
            if (price != null && precio <= price) {
                // Cambiamos el estado de la subasta para el libro, pues ya esta activa
                actualizarTabla(titulo, precio, "Activa");
                
                // El comprador está interesado en dicho libro que se oferta
                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContent("Pujo!");
                myAgent.send(reply);
            }
            else if(price != null && precio > price){
                actualizarTabla(titulo, precio,  "Precio excesivo");
                reply.setPerformative(ACLMessage.REFUSE);
                reply.setContent("No me interesa");
                myAgent.send(reply);
                gui.getLibros().remove(titulo);
            }
            else{
                reply.setPerformative(ACLMessage.REFUSE);
                reply.setContent("No me interesa");
                myAgent.send(reply);
                gui.getLibros().remove(titulo);
            }
            
        }

        private void actualizarTabla(String titulo, Object Precio, Object estado) {
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
                        // Datos actualizados
                        break; 
                    }
                }
        }
    }

    public ClienteGUI getGui() {
        return gui;
    }
    
    
    
    
    
    
    
    
    
    

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
            System.out.println("Cliente " + this.getAID().getName() + " desregistrado");
        } catch (Exception e) {
            System.out.println("Error al desregistrarse en el DFService" + e.getMessage());
        }
    }
}
