/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui_hlavni.HlavniJFrame;
import gui_okno.Vlastnictvi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import logika.Datum;


/**
 *
 * @author strmiska
 */
public class Pozemky extends javax.swing.JPanel {
    public int objectPK;
    public int personPK;
    public int officerPK;
    public int index = 0;
    public int indexVlastnika = 0;
    public float plocha;
    
    public DefaultListModel<String> listPozemku = new DefaultListModel<>();
    public DefaultListModel<String> listVlastniku = new DefaultListModel<>();
    public Datum d;
    /**
     * Creates new form Pozemky
     */
    public Pozemky() {
        initComponents();
    }
    public void inicializuj(){
            zjistiPKObjektu();
            naplnJListPozemku();  
            zobrazPozemek();
    }
    
    public void naplnJListPozemku(){
        String sql = "select Object_pk, OID, Surface from KAT_MAP_OBJECTS where type = 0 AND VTO = future() ORDER BY OID";
        listPozemku.clear();
        List_seznamPozemku.setModel(listPozemku);
        
        try (Statement stmt = HlavniJFrame.dtb.getConnection().createStatement())
            { 
                try (ResultSet rs = stmt.executeQuery(sql))
                {
                    while (rs.next()) {
                        listPozemku.addElement(rs.getString(2));
                    }
                }
            } catch (SQLException sqlEx) {
                    System.err.println("SQLException (Search-loadShapesFromDb()): " + sqlEx.getMessage());
        }
        
//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//            listPozemku.addElement(rs.getString(1));
//                        }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
        

        List_seznamPozemku.setModel(listPozemku);        
    }
    
    public void zobrazPozemek(){

        listVlastniku.clear();
        
     String sql = "select KAT_PERSONS.PERSON_PK, KAT_persons.name,  KAT_persons.Surname,Kat_ownership.property_share, KAT_OFFICERS.OFFICER_PK, \n" +
"(select Kat_persons.name FROM KAT_PERSONS, KAT_OFFICERS WHERE KAT_PERSONS.PERSON_PK = KAT_OFFICERS.OFFICER_PK and KAT_OWNERSHIP.OFFICER_FK = KAT_OFFICERS.OFFICER_PK)UrednikName, \n" +
"(select Kat_persons.SURNAME FROM KAT_PERSONS, KAT_OFFICERS WHERE KAT_PERSONS.PERSON_PK = KAT_OFFICERS.OFFICER_PK and KAT_OWNERSHIP.OFFICER_FK = KAT_OFFICERS.OFFICER_PK)UrednikPrijmeni\n" +
", Kat_ownership.VFROM FROM KAT_MAP_OBJECTS\n" +
"INNER JOIN KAT_OWNERSHIP ON KAT_MAP_OBJECTS.object_pk = KAT_OWNERSHIP.OBJECT_FK\n" +
"INNER JOIN KAT_PERSONS ON KAT_OWNERSHIP.PERSON_FK = KAT_PERSONS.PERSON_PK\n" +
"INNER JOIN KAT_OFFICERS ON KAT_OWNERSHIP.OFFICER_FK = KAT_OFFICERS.OFFICER_PK\n" +
"WHERE OBJECT_PK = ?";

     try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){ 
         pstmt.setInt(1, objectPK);
         ResultSet rs = pstmt.executeQuery();
         if (!rs.isBeforeFirst() ) {    
                label_urednik.setText("Neschváleno");
                Btn_prejdiNaOsobu.setEnabled(false);
                Btn_prejdiNaUrednika.setEnabled(false);
 
            } else{
                Btn_prejdiNaOsobu.setEnabled(true);
                Btn_prejdiNaUrednika.setEnabled(true);
                
            }
            while (rs.next()) {
                    officerPK = rs.getInt(5);
                    label_zacatekVlastnictvi.setText(d.zmenaNaString(rs.getString(8)));
                    label_urednik.setText(rs.getString(6) + " " + rs.getString(7));
                    listVlastniku.addElement(rs.getString(2) +" "+ rs.getString(3) + " " + (rs.getFloat(4))*100 + "%");
             }
     }
    catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }
        indexVlastnika = 0;
        personPK = logika.Dotazy.zjistiPKVlastnika(objectPK, indexVlastnika);
        List_seznamVlastniku.setModel(listVlastniku);
    }
    
    public void zjistiPKObjektu(){
        String sql = "select Object_pk, OID, Surface from KAT_MAP_OBJECTS where type = 0 AND VTO = future() ORDER BY OID";
//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            ResultSet rs = pstmt.executeQuery();            
//            while (rs.next()) {
//               if(rs.getRow() == index +1){
//                    objectPK = rs.getInt(1); 
//               }               
//            }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
        
        try (Statement stmt = HlavniJFrame.dtb.getConnection().createStatement())
            { 
                try (ResultSet rs = stmt.executeQuery(sql))
                {
                   while (rs.next()) {
                     if(rs.getRow() == index +1){
                        objectPK = rs.getInt(1); 
                        label_id.setText(objectPK+"");
                        label_oid.setText(rs.getInt(2)+"");
                        plocha = rs.getInt(3);
                        if(plocha != 0){
                            label_vymera.setText(plocha+" m2");
                            Btn_vypoctiPlochu.setEnabled(false);
                        }else{
                            label_vymera.setText("plocha zatím nevypočítána");
                            Btn_vypoctiPlochu.setEnabled(true);
                        }
                     }               
            }
                }
            } catch (SQLException sqlEx) {
                    System.err.println("SQLException (Search-loadShapesFromDb()): " + sqlEx.getMessage());
            }
        
        
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        List_seznamPozemku = new javax.swing.JList<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        List_seznamVlastniku = new javax.swing.JList<>();
        Btn_prejdiNaUrednika = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        label_id = new javax.swing.JLabel();
        label_oid = new javax.swing.JLabel();
        label_vymera = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        label_urednik = new javax.swing.JLabel();
        Btn_prejdiNaOsobu = new javax.swing.JButton();
        label_zacatekVlastnictvi = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        Btn_prejdiNaVlastnictvi = new javax.swing.JButton();
        Btn_vypoctiPlochu = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(679, 546));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("Seznam pozemků:");

        List_seznamPozemku.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                List_seznamPozemkuValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(List_seznamPozemku);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setText("ID pozemku:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel10.setText("OID pozemku:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText("Výměra:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setText("Seznam vlastníků:");

        List_seznamVlastniku.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                List_seznamVlastnikuValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(List_seznamVlastniku);

        Btn_prejdiNaUrednika.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_prejdiNaUrednika.setText("Přejít na úředníka");
        Btn_prejdiNaUrednika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_prejdiNaUrednikaActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton10.setText("Zobrazit na mapě");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        label_id.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_id.setText("ID");

        label_oid.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_oid.setText("OID");

        label_vymera.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_vymera.setText("plocha zatím nevypočítána");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel15.setText("Pozemek schválil:");

        label_urednik.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_urednik.setText("Úředník");

        Btn_prejdiNaOsobu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_prejdiNaOsobu.setText("Přejít na osobu");
        Btn_prejdiNaOsobu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_prejdiNaOsobuActionPerformed(evt);
            }
        });

        label_zacatekVlastnictvi.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_zacatekVlastnictvi.setText("xxx");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setText("Počátek vlastnictví:");

        Btn_prejdiNaVlastnictvi.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_prejdiNaVlastnictvi.setText("Správa vlastnictví");
        Btn_prejdiNaVlastnictvi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_prejdiNaVlastnictviActionPerformed(evt);
            }
        });

        Btn_vypoctiPlochu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_vypoctiPlochu.setText("Vypočti plochu");
        Btn_vypoctiPlochu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_vypoctiPlochuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10))
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Btn_prejdiNaVlastnictvi)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(label_urednik))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(Btn_prejdiNaOsobu))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9))
                        .addGap(51, 51, 51)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_oid)
                            .addComponent(label_id)
                            .addComponent(label_vymera)
                            .addComponent(Btn_vypoctiPlochu)))
                    .addComponent(Btn_prejdiNaUrednika)
                    .addComponent(jLabel13)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(label_zacatekVlastnictvi)))
                .addContainerGap(189, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(label_id)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_oid)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_vymera)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Btn_vypoctiPlochu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(label_zacatekVlastnictvi))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(label_urednik))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Btn_prejdiNaUrednika)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(Btn_prejdiNaOsobu)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Btn_prejdiNaVlastnictvi)
                            .addComponent(jButton10))
                        .addContainerGap(124, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void Btn_prejdiNaUrednikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_prejdiNaUrednikaActionPerformed
     
        HlavniJFrame.hlavniOkno.okna.zobrazUrednika(officerPK);    
        // TODO add your handling code here:
    }//GEN-LAST:event_Btn_prejdiNaUrednikaActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void List_seznamPozemkuValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_List_seznamPozemkuValueChanged
        index = List_seznamPozemku.getSelectedIndex();
        zjistiPKObjektu();
        zobrazPozemek();
    }//GEN-LAST:event_List_seznamPozemkuValueChanged

    private void Btn_prejdiNaOsobuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_prejdiNaOsobuActionPerformed
        HlavniJFrame.hlavniOkno.okna.zobrazVlastnika(personPK);           // TODO add your handling code here:
    }//GEN-LAST:event_Btn_prejdiNaOsobuActionPerformed

    private void List_seznamVlastnikuValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_List_seznamVlastnikuValueChanged
        indexVlastnika = List_seznamVlastniku.getSelectedIndex() ; 
        personPK = logika.Dotazy.zjistiPKVlastnika(objectPK, indexVlastnika);
        
    }//GEN-LAST:event_List_seznamVlastnikuValueChanged

    private void Btn_prejdiNaVlastnictviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_prejdiNaVlastnictviActionPerformed
          HlavniJFrame.okna.vlastnictvi = new Vlastnictvi(HlavniJFrame.hlavniOkno, true);
          HlavniJFrame.okna.vlastnictvi.initForInsertPozemky(this, objectPK);
          HlavniJFrame.okna.vlastnictvi.setVisible(true);
    }//GEN-LAST:event_Btn_prejdiNaVlastnictviActionPerformed

    private void Btn_vypoctiPlochuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_vypoctiPlochuActionPerformed
        label_vymera.setText(logika.Dotazy.vypoctiPlochu(objectPK)+"");
        Btn_vypoctiPlochu.setEnabled(false);
    }//GEN-LAST:event_Btn_vypoctiPlochuActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Btn_prejdiNaOsobu;
    private javax.swing.JButton Btn_prejdiNaUrednika;
    private javax.swing.JButton Btn_prejdiNaVlastnictvi;
    private javax.swing.JButton Btn_vypoctiPlochu;
    private javax.swing.JList<String> List_seznamPozemku;
    private javax.swing.JList<String> List_seznamVlastniku;
    private javax.swing.JButton jButton10;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel label_id;
    private javax.swing.JLabel label_oid;
    private javax.swing.JLabel label_urednik;
    private javax.swing.JLabel label_vymera;
    private javax.swing.JLabel label_zacatekVlastnictvi;
    // End of variables declaration//GEN-END:variables
}
