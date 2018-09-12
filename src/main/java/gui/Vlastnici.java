/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
import gui_hlavni.HlavniJFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;

/**
 *
 * @author strmiska
 */
public class Vlastnici extends javax.swing.JPanel {
    public int personPK;
    public int pozemekPK;
    public int stavbaPK;
    public int index = 0;
    public int indexPozemku = 0;
    public int indexStavby = 0;
    public DefaultListModel<String> listVlastniku = new DefaultListModel<>();
    public DefaultListModel<String> listPozemku = new DefaultListModel<>();
    public DefaultListModel<String> listStaveb = new DefaultListModel<>();
    public int pocetVlastnenych;
    /**
     * Creates new form Vlastnici
     */
    public Vlastnici() {
        initComponents();
    }
    
    public void inicializuj(){
        zjistiPK();
        zobrazVlastnika();        
        naplnJListVlastniku();  
   }
    
    public void naplnJListVlastniku(){
        String sql = "SELECT DISTINCT PERSON_PK, NAME, SURNAME\n" +
"FROM KAT_PERSONS, KAT_OWNERSHIP\n" +
"WHERE KAT_PERSONS.PERSON_PK = KAT_OWNERSHIP.PERSON_FK\n" +
"ORDER BY SURNAME";
        listVlastniku.clear();
        List_seznamVlastniku.setModel(listVlastniku);

//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//            listVlastniku.addElement(rs.getString(2) +" "+ rs.getString(3));
//                        }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
    try (Statement stmt = HlavniJFrame.dtb.getConnection().createStatement()){ 
        try (ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                listVlastniku.addElement(rs.getString(2) +" "+ rs.getString(3));              
            }
        }
    } catch (SQLException sqlEx) {
         System.err.println("SQLException (Search-loadShapesFromDb()): " + sqlEx.getMessage());
    }
        

        List_seznamVlastniku.setModel(listVlastniku);
        
    }
    
    public void zobrazVlastnika(){
        pocetVlastnenych = 0;
     String sql = "SELECT DISTINCT PERSON_PK, NAME, SURNAME\n" +
    "FROM KAT_PERSONS, KAT_OWNERSHIP\n" +
    "WHERE KAT_PERSONS.PERSON_PK = KAT_OWNERSHIP.PERSON_FK AND KAT_PERSONS.PERSON_PK = ?\n" +
    "ORDER BY SURNAME";
//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, personPK);
//            ResultSet rs = pstmt.executeQuery();
//            
//            while (rs.next()) {
//                    personPK = rs.getInt(1);               
//                    label_jmeno.setText(rs.getString(2));
//                    label_prijmeni.setText(rs.getString(3));
//            }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
        
        try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){ 
        pstmt.setInt(1, personPK);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            personPK = rs.getInt(1);               
            label_jmeno.setText(rs.getString(2));
            label_prijmeni.setText(rs.getString(3));              
        }
    }
    catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }
        zobrazPozemky();
        zobrazStavby();
        zjistiPKPozemku();
        zjistiPKStavby();
        label_pocetVlastnenych.setText(""+pocetVlastnenych);
    }
    
    public void zjistiPK(){
        String sql = "SELECT DISTINCT PERSON_PK, SURNAME\n" +
    "FROM KAT_PERSONS, KAT_OWNERSHIP\n" +
    "WHERE KAT_PERSONS.PERSON_PK = KAT_OWNERSHIP.PERSON_FK\n" +
    "ORDER BY SURNAME";
//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            ResultSet rs = pstmt.executeQuery();
//            
//            while (rs.next()) {
//
//                if(rs.getRow() == index +1){
//                    personPK = rs.getInt(1);               
//                                        
//                }               
//            }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
    try (Statement stmt = HlavniJFrame.dtb.getConnection().createStatement()){ 
        try (ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                if(rs.getRow() == index +1){
                    personPK = rs.getInt(1);               
                                        
                }               
            }
        }
    } catch (SQLException sqlEx) {
         System.err.println("SQLException (Search-loadShapesFromDb()): " + sqlEx.getMessage());
    }
        
    }
    
    
    public void zjistiPKPozemku(){
        String sql = "select DISTINCT KAT_MAP_OBJECTS.OBJECT_PK\n" +
"FROM KAT_PERSONS, KAT_OWNERSHIP, KAT_MAP_OBJECTS\n" +
"WHERE KAT_PERSONS.PERSON_PK = ? AND KAT_MAP_OBJECTS.TYPE = 0 AND KAT_PERSONS.PERSON_PK = KAT_OWNERSHIP.PERSON_FK AND KAT_OWNERSHIP.OBJECT_FK = KAT_MAP_OBJECTS.OBJECT_PK\n" +
"ORDER BY KAT_MAP_OBJECTS.OBJECT_PK";
//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, personPK);
//            ResultSet rs = pstmt.executeQuery();            
//            while (rs.next()) {
//               if(rs.getRow() == indexPozemku +1){
//                    pozemekPK = rs.getInt(1);  
//               }               
//            }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }        
    try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){ 
        pstmt.setInt(1, personPK);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            if(rs.getRow() == indexPozemku +1){
                    pozemekPK = rs.getInt(1);  
               }            
        }
    }
    catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }
    
    }
    
    public void zjistiPKStavby(){
        String sql = "select DISTINCT KAT_MAP_OBJECTS.OBJECT_PK\n" +
"FROM KAT_PERSONS, KAT_OWNERSHIP, KAT_MAP_OBJECTS\n" +
"WHERE KAT_PERSONS.PERSON_PK = ? AND KAT_MAP_OBJECTS.TYPE = 1 AND KAT_PERSONS.PERSON_PK = KAT_OWNERSHIP.PERSON_FK AND KAT_OWNERSHIP.OBJECT_FK = KAT_MAP_OBJECTS.OBJECT_PK\n" +
"ORDER BY KAT_MAP_OBJECTS.OBJECT_PK";
//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, personPK);
//            ResultSet rs = pstmt.executeQuery();            
//            while (rs.next()) {
//               if(rs.getRow() == indexStavby +1){
//                    stavbaPK = rs.getInt(1);  
//               }               
//            }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }

    try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){ 
        pstmt.setInt(1, personPK);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            if(rs.getRow() == indexStavby +1){
                    stavbaPK = rs.getInt(1);  
               }              
        }
    }
    catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }        
    }
    
    public void zobrazPozemky(){
        listPozemku.clear();
        
     String sql = "select DISTINCT KAT_MAP_OBJECTS.OBJECT_PK, KAT_OWNERSHIP.PROPERTY_SHARE\n" +
"FROM KAT_PERSONS, KAT_OWNERSHIP, KAT_MAP_OBJECTS\n" +
"WHERE KAT_PERSONS.PERSON_PK = ? AND KAT_MAP_OBJECTS.TYPE = 0 AND KAT_PERSONS.PERSON_PK = KAT_OWNERSHIP.PERSON_FK AND KAT_OWNERSHIP.OBJECT_FK = KAT_MAP_OBJECTS.OBJECT_PK\n" +
"ORDER BY KAT_MAP_OBJECTS.OBJECT_PK";
//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//                pstmt.setInt(1, personPK);
//                 
//            ResultSet rs = pstmt.executeQuery();
//            if (!rs.isBeforeFirst() ) {    
//                Btn_prejdiNaPozemek.setEnabled(false);
// 
//            } else{
//                Btn_prejdiNaPozemek.setEnabled(true);
//            }
//            while (rs.next()) {
//           
//                    listPozemku.addElement("ID pozemku: "+rs.getString(1)+ ", podíl: "+(rs.getFloat(2))*100 +"%");
//                    pocetVlastnenych++;
//             }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
    
    try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){ 
        pstmt.setInt(1, personPK);
        ResultSet rs = pstmt.executeQuery();
        if (!rs.isBeforeFirst() ) {    
                Btn_prejdiNaPozemek.setEnabled(false);
        } else{
                Btn_prejdiNaPozemek.setEnabled(true);
        }
        while (rs.next()) {
            listPozemku.addElement("ID pozemku: "+rs.getString(1)+ ", podíl: "+(rs.getFloat(2))*100 +"%");
            pocetVlastnenych++;              
        }
    }
    catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }
        
        List_seznamPozemku.setModel(listPozemku);
    }
    
    public void zobrazStavby(){
        listStaveb.clear();
        
     String sql = "select DISTINCT KAT_MAP_OBJECTS.OBJECT_PK, KAT_OWNERSHIP.PROPERTY_SHARE\n" +
"FROM KAT_PERSONS, KAT_OWNERSHIP, KAT_MAP_OBJECTS\n" +
"WHERE KAT_PERSONS.PERSON_PK = ? AND KAT_MAP_OBJECTS.TYPE = 1 AND KAT_PERSONS.PERSON_PK = KAT_OWNERSHIP.PERSON_FK AND KAT_OWNERSHIP.OBJECT_FK = KAT_MAP_OBJECTS.OBJECT_PK\n" +
"ORDER BY KAT_MAP_OBJECTS.OBJECT_PK";
//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//                pstmt.setInt(1, personPK);
//                 
//            ResultSet rs = pstmt.executeQuery();
//            if (!rs.isBeforeFirst() ) {    
//                Btn_prejdiNaStavbu.setEnabled(false);
// 
//            } else{
//                Btn_prejdiNaStavbu.setEnabled(true);
//            }
//            while (rs.next()) {
//                    pocetVlastnenych++;
//                    listStaveb.addElement("ID stavby: "+rs.getString(1)+ ", podíl: "+(rs.getFloat(2))*100 +"%");
//             }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
    try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){ 
        pstmt.setInt(1, personPK);
        ResultSet rs = pstmt.executeQuery();
        if (!rs.isBeforeFirst() ) {    
            Btn_prejdiNaStavbu.setEnabled(false);
        } else{
                Btn_prejdiNaStavbu.setEnabled(true);
            }
        while (rs.next()) {
            pocetVlastnenych++;
            listStaveb.addElement("ID stavby: "+rs.getString(1)+ ", podíl: "+(rs.getFloat(2))*100 +"%");             
        }
    }
    catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }
        
        List_seznamStaveb.setModel(listStaveb);
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
        List_seznamVlastniku = new javax.swing.JList<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        label_jmeno = new javax.swing.JLabel();
        label_prijmeni = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        label_pocetVlastnenych = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        List_seznamPozemku = new javax.swing.JList<>();
        Btn_prejdiNaPozemek = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        List_seznamStaveb = new javax.swing.JList<>();
        Btn_prejdiNaStavbu = new javax.swing.JButton();
        Btn_prejdiNaOsobu = new javax.swing.JButton();

        List_seznamVlastniku.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                List_seznamVlastnikuValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(List_seznamVlastniku);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Seznam všech vlastníků:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Příjmení:");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Jméno:");

        label_jmeno.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_jmeno.setText("Jméno");

        label_prijmeni.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_prijmeni.setText("Příjmení");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText("Počet vlastněných");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setText("nemovitostí:");

        label_pocetVlastnenych.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_pocetVlastnenych.setText("0");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setText("Seznam vlastněných pozemků:");

        List_seznamPozemku.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                List_seznamPozemkuValueChanged(evt);
            }
        });
        jScrollPane5.setViewportView(List_seznamPozemku);

        Btn_prejdiNaPozemek.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_prejdiNaPozemek.setText("přejít na pozemek");
        Btn_prejdiNaPozemek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_prejdiNaPozemekActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel15.setText("Seznam vlastněných staveb:");

        List_seznamStaveb.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                List_seznamStavebValueChanged(evt);
            }
        });
        jScrollPane6.setViewportView(List_seznamStaveb);

        Btn_prejdiNaStavbu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_prejdiNaStavbu.setText("přejít na stavbu");
        Btn_prejdiNaStavbu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_prejdiNaStavbuActionPerformed(evt);
            }
        });

        Btn_prejdiNaOsobu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_prejdiNaOsobu.setText("přejít na osobu");
        Btn_prejdiNaOsobu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_prejdiNaOsobuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_jmeno)
                            .addComponent(label_prijmeni)))
                    .addComponent(jLabel12)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_pocetVlastnenych))
                    .addComponent(jLabel14)
                    .addComponent(Btn_prejdiNaOsobu)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Btn_prejdiNaPozemek)
                            .addComponent(Btn_prejdiNaStavbu))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(label_jmeno))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label_prijmeni)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Btn_prejdiNaOsobu)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(label_pocetVlastnenych))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Btn_prejdiNaPozemek)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(99, 99, 99)
                                        .addComponent(Btn_prejdiNaStavbu))))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(56, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void List_seznamVlastnikuValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_List_seznamVlastnikuValueChanged
        index = List_seznamVlastniku.getSelectedIndex();
        zjistiPK();
        zobrazVlastnika();          // TODO add your handling code here:
    }//GEN-LAST:event_List_seznamVlastnikuValueChanged

    private void Btn_prejdiNaPozemekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_prejdiNaPozemekActionPerformed
        HlavniJFrame.hlavniOkno.okna.zobrazPozemek(pozemekPK);        // TODO add your handling code here:
    }//GEN-LAST:event_Btn_prejdiNaPozemekActionPerformed

    private void Btn_prejdiNaStavbuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_prejdiNaStavbuActionPerformed
        HlavniJFrame.hlavniOkno.okna.zobrazStavbu(stavbaPK);// TODO add your handling code here:
    }//GEN-LAST:event_Btn_prejdiNaStavbuActionPerformed

    private void Btn_prejdiNaOsobuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_prejdiNaOsobuActionPerformed
        
        HlavniJFrame.hlavniOkno.okna.zobrazOsobu(personPK);        // TODO add your handling code here:
    }//GEN-LAST:event_Btn_prejdiNaOsobuActionPerformed

    private void List_seznamPozemkuValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_List_seznamPozemkuValueChanged
        indexPozemku = List_seznamPozemku.getSelectedIndex() ; 
        zjistiPKPozemku();        // TODO add your handling code here:
    }//GEN-LAST:event_List_seznamPozemkuValueChanged

    private void List_seznamStavebValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_List_seznamStavebValueChanged
        indexStavby = List_seznamPozemku.getSelectedIndex() ; 
        zjistiPKStavby();// TODO add your handling code here:
    }//GEN-LAST:event_List_seznamStavebValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Btn_prejdiNaOsobu;
    private javax.swing.JButton Btn_prejdiNaPozemek;
    private javax.swing.JButton Btn_prejdiNaStavbu;
    private javax.swing.JList<String> List_seznamPozemku;
    private javax.swing.JList<String> List_seznamStaveb;
    private javax.swing.JList<String> List_seznamVlastniku;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    public javax.swing.JLabel label_jmeno;
    public javax.swing.JLabel label_pocetVlastnenych;
    public javax.swing.JLabel label_prijmeni;
    // End of variables declaration//GEN-END:variables
}
