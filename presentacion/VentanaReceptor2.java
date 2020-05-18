package presentacion;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;

import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.sound.sampled.Clip;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import javax.swing.table.DefaultTableModel;

import negocio.Mensaje;
import negocio.NegocioReceptor;
import negocio.Persona;


/**
 *
 * @author Admin
 */
public class VentanaReceptor2 extends javax.swing.JFrame implements IVentanaReceptor {
    
    private DefaultTableModel listaRecibidos;
    private Clip clip;
    
    /**
     * Creates new form Home
     */
    public VentanaReceptor2() {
        listaRecibidos = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
               //all cells false
               return false;
            }
        };
        listaRecibidos.addColumn("Fecha");
        listaRecibidos.addColumn("Emisor");
        listaRecibidos.addColumn("Asunto");
        listaRecibidos.addColumn("Texto");
        listaRecibidos.addColumn("");
        initComponents();
        this.botonAbrir.setActionCommand("ABRIR MENSAJE");
        this.cerrarMensaje.setActionCommand("CERRAR MENSAJE");
        this.botonPararAlerta.setActionCommand("PARAR ALERTA");
        this.btConfirmarReceptor.setActionCommand("ACEPTAR SESION");
        this.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        NegocioReceptor.getInstancia().notificarDesconexionDirectorio();
                        System.exit(0);
                    }
        });
        setColor(btn_1); 
        ind_1.setOpaque(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        jDialog1 = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        labelDestinatario = new javax.swing.JLabel();
        labelFecha1 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        labelAsunto = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        textoMsjRecibido = new javax.swing.JTextArea();
        cerrarMensaje = new javax.swing.JButton();
        jOptionPane1 = new javax.swing.JOptionPane();
        jdiagAlertaSonora = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jlabelmsjent = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        botonPararAlerta = new javax.swing.JButton();
        jDiagSesionRecep = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        nombreReceptor = new javax.swing.JTextArea();
        btConfirmarReceptor = new javax.swing.JButton();
        side_pane = new javax.swing.JPanel();
        btn_1 = new javax.swing.JPanel();
        ind_1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        btn_exit = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel_usrname = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaMensajesRecibidos = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        botonAbrir = new javax.swing.JButton();

        jDialog1.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(0, 51, 204));

        labelDestinatario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelDestinatario.setForeground(new java.awt.Color(255, 255, 255));
        labelDestinatario.setText("De: Ivan Aprea");

        labelFecha1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        labelFecha1.setForeground(new java.awt.Color(255, 255, 255));
        labelFecha1.setText("Fecha");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelDestinatario, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(188, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                    .addContainerGap(260, Short.MAX_VALUE)
                    .addComponent(labelFecha1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(22, 22, 22)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelDestinatario, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(labelFecha1, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jDialog1.getContentPane().add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 430, 50));

        jPanel11.setBackground(new java.awt.Color(0, 0, 51));

        labelAsunto.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelAsunto.setForeground(new java.awt.Color(255, 255, 255));
        labelAsunto.setText("Asunto");

        textoMsjRecibido.setEditable(false);
        textoMsjRecibido.setColumns(20);
        textoMsjRecibido.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        textoMsjRecibido.setRows(5);
        jScrollPane3.setViewportView(textoMsjRecibido);

        cerrarMensaje.setText("Cerrar");
        cerrarMensaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarMensajeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cerrarMensaje)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addComponent(labelAsunto, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(127, Short.MAX_VALUE)))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(cerrarMensaje)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addComponent(labelAsunto, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(279, Short.MAX_VALUE)))
        );

        jDialog1.getContentPane().add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 430, 340));

        jdiagAlertaSonora.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(0, 51, 204));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Mensaje entrante");

        jlabelmsjent.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jlabelmsjent.setForeground(new java.awt.Color(255, 255, 255));
        jlabelmsjent.setText("De: ...");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jLabel9))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jlabelmsjent)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlabelmsjent)
                .addGap(6, 6, 6))
        );

        jdiagAlertaSonora.getContentPane().add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 60));

        jPanel9.setBackground(new java.awt.Color(0, 51, 153));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-alert-50.png"))); // NOI18N

        botonPararAlerta.setText("Parar");
        botonPararAlerta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPararAlertaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(botonPararAlerta, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(botonPararAlerta)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jdiagAlertaSonora.getContentPane().add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 59, 180, 140));

        jDiagSesionRecep.setAutoRequestFocus(false);
        jDiagSesionRecep.setBackground(new java.awt.Color(0, 0, 204));
        jDiagSesionRecep.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 51, 204));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Inicio de sesion");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(jLabel5)
                .addContainerGap(89, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        jDiagSesionRecep.getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 60));

        jPanel7.setBackground(new java.awt.Color(102, 102, 255));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/imge_1.png"))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Nombre:");

        nombreReceptor.setColumns(20);
        nombreReceptor.setRows(1);
        jScrollPane2.setViewportView(nombreReceptor);

        btConfirmarReceptor.setText("Iniciar sesion");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(btConfirmarReceptor))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btConfirmarReceptor)
                .addGap(51, 51, 51))
        );

        jDiagSesionRecep.getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 270, 210));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setLocationByPlatform(true);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        side_pane.setBackground(new java.awt.Color(23, 35, 51));
        side_pane.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_1.setBackground(new java.awt.Color(23, 35, 51));
        btn_1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_1MousePressed(evt);
            }
        });

        ind_1.setOpaque(false);
        ind_1.setPreferredSize(new java.awt.Dimension(3, 43));

        javax.swing.GroupLayout ind_1Layout = new javax.swing.GroupLayout(ind_1);
        ind_1.setLayout(ind_1Layout);
        ind_1Layout.setHorizontalGroup(
            ind_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );
        ind_1Layout.setVerticalGroup(
            ind_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 43, Short.MAX_VALUE)
        );

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Home");

        javax.swing.GroupLayout btn_1Layout = new javax.swing.GroupLayout(btn_1);
        btn_1.setLayout(btn_1Layout);
        btn_1Layout.setHorizontalGroup(
            btn_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_1Layout.createSequentialGroup()
                .addComponent(ind_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel8)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        btn_1Layout.setVerticalGroup(
            btn_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_1Layout.createSequentialGroup()
                .addComponent(ind_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(btn_1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        side_pane.add(btn_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 120, -1));

        getContentPane().add(side_pane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 590));

        jPanel2.setBackground(new java.awt.Color(71, 120, 197));
        jPanel2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel2MouseDragged(evt);
            }
        });
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel2MousePressed(evt);
            }
        });
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("SMU - Bandeja de entrada");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 430, 30));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Secured_Letter_25px_2.png"))); // NOI18N
        jLabel4.setToolTipText("");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 30, 30));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, 950, 50));

        jPanel3.setBackground(new java.awt.Color(71, 120, 197));

        jPanel4.setBackground(new java.awt.Color(120, 168, 252));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Exit_25px.png"))); // NOI18N
        btn_exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_exitMousePressed(evt);
            }
        });
        jPanel4.add(btn_exit, new org.netbeans.lib.awtextra.AbsoluteConstraints(239, 24, -1, 46));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/imge_1.png"))); // NOI18N
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 86, 80));

        jLabel_usrname.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_usrname.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_usrname.setText("Default_usr");
        jPanel4.add(jLabel_usrname, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, -1, 30));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(460, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 300, 540));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        listaMensajesRecibidos.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        listaMensajesRecibidos.setModel(listaRecibidos);
        listaMensajesRecibidos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        listaMensajesRecibidos.setGridColor(new java.awt.Color(255, 255, 255));
        listaMensajesRecibidos.setRowHeight(22);
        listaMensajesRecibidos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(listaMensajesRecibidos);

        jPanel6.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 650, 500));

        jPanel12.setBackground(new java.awt.Color(84, 127, 206));

        botonAbrir.setText("Abrir mensaje");
        botonAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAbrirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(521, Short.MAX_VALUE)
                .addComponent(botonAbrir)
                .addGap(49, 49, 49))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonAbrir)
                .addContainerGap())
        );

        jPanel6.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 500, 650, 40));

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, 650, 540));

        pack();
    }//GEN-END:initComponents

    int xx,xy;
    private void btn_exitMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_exitMousePressed
        NegocioReceptor.getInstancia().notificarDesconexionDirectorio();
        System.exit(0);
    }//GEN-LAST:event_btn_exitMousePressed

    private void jPanel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MousePressed
        // TODO add your handling code here:
        //drag this pane
        xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_jPanel2MousePressed

    private void jPanel2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseDragged
        // TODO add your handling code here:

        //source to drag
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x-xx,y-xy);
    }//GEN-LAST:event_jPanel2MouseDragged

    private void botonPararAlertaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPararAlertaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonPararAlertaActionPerformed

    private void cerrarMensajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarMensajeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cerrarMensajeActionPerformed

    private void botonAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAbrirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonAbrirActionPerformed

    private void btn_1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_1MousePressed
        // TODO add your handling code here:
        setColor(btn_1);
        ind_1.setOpaque(true);
    }//GEN-LAST:event_btn_1MousePressed

/*

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(presentacion.VentanaReceptor2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(presentacion.VentanaReceptor2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(presentacion.VentanaReceptor2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(presentacion.VentanaReceptor2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new presentacion.VentanaReceptor2().setVisible(true);
            }
        });
    }
   */
    
    
    public Clip getClip() {
                return this.clip;
        }

        public void setClip(Clip clip) {
                this.clip = clip;
        }

    public void abrirMensaje() {
        int row = listaMensajesRecibidos.getSelectedRow();
        if(row != -1) {
            this.actualizaMensajeAbierto(
                listaMensajesRecibidos.getValueAt(row, 0).toString(), //fecha
                listaMensajesRecibidos.getValueAt(row, 1).toString(), //emisor
                listaMensajesRecibidos.getValueAt(row, 2).toString(), //asunto
                listaMensajesRecibidos.getValueAt(row, 3).toString()); //texto
            this.jDialog1.setSize(435,420);
            this.jDialog1.setResizable(false);
            this.jDialog1.setVisible(true);
            this.repaint();
        }
    }
    
    public void iniciarSesion() {
        this.jDiagSesionRecep.setSize(275, 305);
        this.jDiagSesionRecep.setResizable(false);
        this.jDiagSesionRecep.setVisible(true);
        this.nombreReceptor.setText(NegocioReceptor.getInstancia().getNombre());
        //this.jLabel_usrname.setText(NegocioReceptor.getInstancia().getNombre());
        this.repaint();
    }
    
    public void confirmarSesion() {
        String nombre = this.nombreReceptor.getText().trim();
        if(!nombre.equals(""))
        {
            NegocioReceptor.getInstancia().configAtributos(nombre);
            this.jLabel_usrname.setText(nombre);
            this.jDiagSesionRecep.setVisible(false);
            NegocioReceptor.getInstancia().iniciarSesion();
            this.setVisible(true);
        }
        else {
                this.lanzarCartelError("ERROR: debe completar todos los campos");
        }
    }
    
    public void mostrarVentana(){
        this.setVisible(true);
        this.repaint();
    }
    
    public void lanzarAlerta(String emisor) {
        this.jlabelmsjent.setText("De: "+emisor);
        this.jdiagAlertaSonora.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.jdiagAlertaSonora.setSize(185,230);
        this.jdiagAlertaSonora.setResizable(false);
        this.jdiagAlertaSonora.setVisible(true);
        this.clip.start();
        this.repaint();
    }
    
    public void cerrarMensaje() {
        this.jDialog1.setVisible(false);
    }
    
    public void pararAlerta() {
        this.clip.stop();
        this.jdiagAlertaSonora.setVisible(false);
    }
    
    public void actualizaListaMensajes(Mensaje mensaje)
    {
        this.listaRecibidos.addRow(new Object[]{mensaje.getFecha(), mensaje.getEmisor().getNombre(), mensaje.getAsunto(), mensaje.getTexto(), ((mensaje.getTipo() == 1) ? "[!]" : "")});
        this.repaint();
    }
    
    public void lanzarCartelError(String err) {
        JOptionPane.showMessageDialog(null, err);
    }
    
    public void actualizaMensajeAbierto(String fecha, String emisorAct, String asuntoAct, String textoAct)
    {
        this.labelFecha1.setText(fecha);
        this.labelDestinatario.setText("De: "+emisorAct);
        this.labelAsunto.setText("Asunto: "+asuntoAct);
        this.textoMsjRecibido.setText(textoAct);
    }

    public JDialog getJDiagSesionRecep() {
        return jDiagSesionRecep;
    }
    
    
    private void setColor(JPanel pane)
    {
        pane.setBackground(new Color(41,57,80));
    }
    

    @Override
    public void addActionListener(ActionListener actionListener) {
            this.botonAbrir.addActionListener(actionListener);
            this.cerrarMensaje.addActionListener(actionListener);
            this.botonPararAlerta.addActionListener(actionListener);
            this.btConfirmarReceptor.addActionListener(actionListener);
            
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonAbrir;
    private javax.swing.JButton botonPararAlerta;
    private javax.swing.JButton btConfirmarReceptor;
    private javax.swing.JPanel btn_1;
    private javax.swing.JLabel btn_exit;
    private javax.swing.JButton cerrarMensaje;
    private javax.swing.JPanel ind_1;
    private javax.swing.JDialog jDiagSesionRecep;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_usrname;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JDialog jdiagAlertaSonora;
    private javax.swing.JLabel jlabelmsjent;
    private javax.swing.JLabel labelAsunto;
    private javax.swing.JLabel labelDestinatario;
    private javax.swing.JLabel labelFecha1;
    private javax.swing.JTable listaMensajesRecibidos;
    private javax.swing.JTextArea nombreReceptor;
    private javax.swing.JPanel side_pane;
    private javax.swing.JTextArea textoMsjRecibido;
    // End of variables declaration//GEN-END:variables
}
