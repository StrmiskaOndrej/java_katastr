/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
import gui_hlavni.HlavniJFrame;
import gui_okno.Vloz_osobu;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import javax.swing.DefaultListModel;

/**
 *
 * @author strmiska
 */
public class VsechnyOsoby extends javax.swing.JPanel {
     public int index = 0;
     public int personPK;
     public HlavniJFrame hlavni;
   //  DefaultListModel listOsob = new DefaultListModel();

     public DefaultListModel<String> listOsob = new DefaultListModel<>();
   
    /**
     * Creates new form Vlastnici
     */
    public VsechnyOsoby() {
        initComponents();
             
    }
    
    public void inicializuj(){
        zjistiPK();
        zobrazOsobu();        
        naplnJListOsob();  
        
    }
    
    /**
     * Naplní DefaultListModel listOsob jmény a přijmeními z databáze pomocí dotazu a seřadí osoby podle abecedy, zároveň tento list převede do jListu
     */
    public void naplnJListOsob(){
        String sql = "select * from KAT_PERSONS ORDER BY SURNAME";
        listOsob.clear();
        List_seznamOsob.setModel(listOsob);

//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//            listOsob.addElement(rs.getString(2) +" "+ rs.getString(3));
//                        }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
    try (Statement stmt = HlavniJFrame.dtb.getConnection().createStatement()){ 
        try (ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                listOsob.addElement(rs.getString(2) +" "+ rs.getString(3));             
            }
        }
    } catch (SQLException sqlEx) {
         System.err.println("SQLException (Search-loadShapesFromDb()): " + sqlEx.getMessage());
    }

        List_seznamOsob.setModel(listOsob);
        
    }
    
    /**
     * Zobrazí informace o osobě v okně Všechny osoby.
     */
    public void zobrazOsobu(){
        
     String sql = "select * from KAT_PERSONS WHERE PERSON_PK = ?";
//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, personPK);
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//                    label_jmeno.setText(rs.getString(2));
//                    label_prijmeni.setText(rs.getString(3));
//                    label_kontakt.setText(rs.getString(5));
//                    label_DN.setText(rs.getString(6));
//                    label_RC.setText(rs.getString(7));
//                    label_pohlavi.setText(Vloz_osobu.pohlaviNaString(rs.getInt(8)));
//                  }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
    
    
    try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){ 
        pstmt.setInt(1, personPK);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
                label_jmeno.setText(rs.getString(2));
                label_prijmeni.setText(rs.getString(3));
                label_kontakt.setText(rs.getString(5));
                label_DN.setText(rs.getString(6));
                label_RC.setText(rs.getString(7));
                label_pohlavi.setText(Vloz_osobu.pohlaviNaString(rs.getInt(8)));             
        }
    }
    catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }
    }
    
    
    public void zjistiPK(){
        String sql = "select KAT_PERSONS.person_pk from KAT_PERSONS ORDER BY SURNAME";
//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//                if(rs.getRow() == index +1){
//                    personPK = rs.getInt(1);               
//                }               
//        //       listOsob.addElement(rs.getString(2) +" "+ rs.getString(3));
//                        }
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        List_seznamOsob = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Btn_UpravitVlastnika = new javax.swing.JButton();
        Btn_evidujOsobu = new javax.swing.JButton();
        Btn_OdstanitVlastnika = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        label_pohlavi = new javax.swing.JLabel();
        label_RC = new javax.swing.JLabel();
        label_DN = new javax.swing.JLabel();
        label_kontakt = new javax.swing.JLabel();
        label_prijmeni = new javax.swing.JLabel();
        label_jmeno = new javax.swing.JLabel();

        List_seznamOsob.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        List_seznamOsob.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        List_seznamOsob.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                List_seznamOsobValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(List_seznamOsob);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Jméno:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Příjmení:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Kontakt:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Datum narození:");

        Btn_UpravitVlastnika.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_UpravitVlastnika.setText("Upravit Osobu");
        Btn_UpravitVlastnika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_UpravitVlastnikaActionPerformed(evt);
            }
        });

        Btn_evidujOsobu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_evidujOsobu.setText("Evidovat Osobu");
        Btn_evidujOsobu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_evidujOsobuActionPerformed(evt);
            }
        });

        Btn_OdstanitVlastnika.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_OdstanitVlastnika.setText("Odstranit Osobu");
        Btn_OdstanitVlastnika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_OdstanitVlastnikaActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Seznam všech osob:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("Pohlaví:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("Rodné číslo:");

        label_pohlavi.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_pohlavi.setText("Pohlaví");

        label_RC.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_RC.setText("Rodné číslo");

        label_DN.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_DN.setText("Datum narození");

        label_kontakt.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_kontakt.setText("Kontakt");

        label_prijmeni.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_prijmeni.setText("Příjmení");

        label_jmeno.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_jmeno.setText("Jméno");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Btn_evidujOsobu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Btn_UpravitVlastnika)
                        .addGap(49, 49, 49)
                        .addComponent(Btn_OdstanitVlastnika))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_jmeno)
                            .addComponent(label_kontakt)
                            .addComponent(label_DN)
                            .addComponent(label_RC)
                            .addComponent(label_pohlavi)
                            .addComponent(label_prijmeni))))
                .addGap(89, 89, 89))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(label_jmeno)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label_prijmeni)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(label_kontakt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_DN)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_RC)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_pohlavi)))))
                .addGap(70, 70, 70)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Btn_UpravitVlastnika)
                    .addComponent(Btn_OdstanitVlastnika)
                    .addComponent(Btn_evidujOsobu))
                .addGap(34, 34, 34))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Zavolá příkaz na odstranění osoby z databáze, následně aktualizuje listOsob a zobrazí jinou osobu
     * @param evt 
     */
    private void Btn_OdstanitVlastnikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_OdstanitVlastnikaActionPerformed
         String sql = "DELETE FROM KAT_PERSONS WHERE BIRTH_ID = ?";
//         try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, label_RC.getText());
//        ResultSet rs = pstmt.executeQuery();
//        conn.close();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } 
    try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){
        pstmt.setString(1, label_RC.getText());
        pstmt.executeUpdate();
    }catch(SQLException SQLEx){
        System.err.println("SQLxception: " + SQLEx.getMessage());
    }
        naplnJListOsob();
        zobrazOsobu();
    }//GEN-LAST:event_Btn_OdstanitVlastnikaActionPerformed

    /**
     * Otovře okno pro evidování nové osoby
     * @param evt 
     */
    private void Btn_evidujOsobuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_evidujOsobuActionPerformed
      
       HlavniJFrame.okna.osoba = new Vloz_osobu(HlavniJFrame.hlavniOkno, true);
       
       HlavniJFrame.okna.osoba.initForInsert(this, false);
       HlavniJFrame.okna.osoba.setVisible(true);
       
    }//GEN-LAST:event_Btn_evidujOsobuActionPerformed

    /**
     * Otovře okno pro úpravu již evidováné osoby
     * @param evt 
     */
    private void Btn_UpravitVlastnikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_UpravitVlastnikaActionPerformed
       HlavniJFrame.okna.osoba = new Vloz_osobu(HlavniJFrame.hlavniOkno, true);
       
       HlavniJFrame.okna.osoba.initForInsert(this, true);
       HlavniJFrame.okna.osoba.setVisible(true);
    }//GEN-LAST:event_Btn_UpravitVlastnikaActionPerformed

    /**
     * Zobrazí novou osobu při přepnutí osoby v JListu
     * @param evt 
     */
    private void List_seznamOsobValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_List_seznamOsobValueChanged
        index = List_seznamOsob.getSelectedIndex();
        zjistiPK();
        zobrazOsobu();
    }//GEN-LAST:event_List_seznamOsobValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Btn_OdstanitVlastnika;
    private javax.swing.JButton Btn_UpravitVlastnika;
    private javax.swing.JButton Btn_evidujOsobu;
    public javax.swing.JList<String> List_seznamOsob;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel label_DN;
    public javax.swing.JLabel label_RC;
    public javax.swing.JLabel label_jmeno;
    public javax.swing.JLabel label_kontakt;
    public javax.swing.JLabel label_pohlavi;
    public javax.swing.JLabel label_prijmeni;
    // End of variables declaration//GEN-END:variables
}
