
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class VendedorGUI extends javax.swing.JFrame {
    private HashMap<String, Float> libros;
    private Vendedor vendedor;
    private DefaultTableModel tblModel;
    private String[] header = {"Libro", "Precio", "Estado", "Ganador"};
    
    public VendedorGUI(Vendedor vendedor) {
        initComponents();
        initTable();
        this.libros = new HashMap();
        this.vendedor = vendedor;
        this.errorTag.setVisible(false);
        this.pack();
        this.setVisible(true);
        this.toFront();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                vendedor.doDelete();
                dispose();
            }
        });
    }

    public HashMap<String, Float> getLibros() {
        return libros;
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        nombreLabel = new javax.swing.JLabel();
        inputPrecioBase = new javax.swing.JTextField();
        precioBaseLabel = new javax.swing.JLabel();
        inputNombre = new javax.swing.JTextField();
        CrearPujaDeLibro = new javax.swing.JButton();
        errorTag = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblRegister = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        nombreLabel.setText("Nombre del libro");

        inputPrecioBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputPrecioBaseActionPerformed(evt);
            }
        });

        precioBaseLabel.setText("Precio base");

        inputNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputNombreActionPerformed(evt);
            }
        });

        CrearPujaDeLibro.setText("Crear puja");
        CrearPujaDeLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CrearPujaDeLibroActionPerformed(evt);
            }
        });

        tblRegister.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tblRegister);

        jLabel1.setText("Subastas");

        jLabel2.setFont(new java.awt.Font("Noto Sans", 1, 24)); // NOI18N
        jLabel2.setText("Casa de subastas");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nombreLabel)
                                    .addComponent(precioBaseLabel))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(errorTag, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(CrearPujaDeLibro))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(inputPrecioBase)
                                        .addComponent(inputNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(207, 207, 207)
                        .addComponent(jLabel2)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombreLabel)
                    .addComponent(inputNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(precioBaseLabel)
                    .addComponent(inputPrecioBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CrearPujaDeLibro)
                    .addComponent(errorTag))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inputPrecioBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputPrecioBaseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputPrecioBaseActionPerformed

    private void inputNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputNombreActionPerformed
    
    // Método que crea una puja con un libro especificado en los datos introducidos
    // en los campos correspondientes
    private void CrearPujaDeLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CrearPujaDeLibroActionPerformed
        try{
            String libro = this.inputNombre.getText();
            String precioStr = this.inputPrecioBase.getText();
            String estado = "Esperando";
            Float precio = Float.valueOf(precioStr);
            
            // Introducimos el libro subastado en las variables pertinentes y a la tabla de la GUI
            if (libro != null && precio != 0 && !this.libros.containsKey(libro)){
                this.vendedor.anadirLibroSubasta(precio, libro);
                this.libros.put(libro, precio);
                this.tblModel.addRow(this.toArray(libro, precioStr, estado, null));
                this.errorTag.setVisible(true);
                this.errorTag.setForeground(new java.awt.Color(0, 255, 0));
                this.errorTag.setText("Libro añadido correctamente");
            }
            else{
                this.errorTag.setVisible(true);
                this.errorTag.setForeground(new java.awt.Color(255, 0, 0));
                this.errorTag.setText("Libro en puja o campos erróneos");
            }
        }catch(Exception e){
            this.errorTag.setVisible(true);
            this.errorTag.setForeground(new java.awt.Color(255, 0, 0));
            this.errorTag.setText("Libro en puja o campos erróneos");
        }
    }//GEN-LAST:event_CrearPujaDeLibroActionPerformed

    public static void iniciar() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VendedorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VendedorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VendedorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VendedorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CrearPujaDeLibro;
    private javax.swing.JLabel errorTag;
    private javax.swing.JTextField inputNombre;
    private javax.swing.JTextField inputPrecioBase;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel nombreLabel;
    private javax.swing.JLabel precioBaseLabel;
    private javax.swing.JTable tblRegister;
    // End of variables declaration//GEN-END:variables

    private void initTable() {
        tblModel = new DefaultTableModel(header, 0);
        tblRegister.setModel(tblModel);
        
    }

    private Object[] toArray(String libro, String precioStr, String estado, String ganador) {
        Object[] obj = new Object [3];
        obj[0] = libro;
        obj[1] = precioStr;
        obj[2] = estado;
        if (ganador != null){
            obj[3] = ganador;
        }
        return obj;
    }

    public JTable getTblModel() {
        return tblRegister;
    }
    
    
}
