/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui_hlavni.HlavniJFrame;
import gui_okno.Vloz_urednika1;
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
public class Urednici extends javax.swing.JPanel {
    private VsechnyOsoby vsechnyOsoby;
     public DefaultListModel<String> listUredniku = new DefaultListModel<>();
     public DefaultListModel<String> listPozemku = new DefaultListModel<>();
     public DefaultListModel<String> listStaveb = new DefaultListModel<>();
     public int personPK;
     public int pozemekPK;
     public int stavbaPK;
     
     public int index = 0;
     public int indexPozemku = 0;
     public int indexStavby = 0;
     public int pocetSchvalenych;
    /**
     * Creates new form Urednici
     */
    public Urednici() {
        initComponents();            
       }
    
    public void inicializuj(){
        zjistiPK();
        naplnJListUredniku();
        zobrazUrednika();
    }
    
    /**
     * Naplní DefaultListModel listUredmiku jmény a přijmeními všech úředníků z databáze pomocí dotazu a seřadí osoby podle abecedy, zároveň tento list převede do jListu
     */
    public void naplnJListUredniku(){
        String sql = "select person_pk, NAME, SURNAME, login from KAT_OFFICERS, KAT_PERSONS where KAT_OFFICERS.OFFICER_PK = KAT_PERSONS.PERSON_PK ORDER BY SURNAME";
        listUredniku.clear();
        List_seznamUredniku.setModel(listUredniku);

//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//            listUredniku.addElement(rs.getString(2) +" "+ rs.getString(3));
//                        }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
        
        try (Statement stmt = HlavniJFrame.dtb.getConnection().createStatement()){ 
        try (ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                  listUredniku.addElement(rs.getString(2) +" "+ rs.getString(3));             
            }
        }
    } catch (SQLException sqlEx) {
         System.err.println("SQLException (Search-loadShapesFromDb()): " + sqlEx.getMessage());
    }
        

        List_seznamUredniku.setModel(listUredniku);
        
    }
    
    /**
     * Zobrazí úředníka v okně úředníků.
     */
    public void zobrazUrednika(){
        pocetSchvalenych = 0;
        
    String sql = "select person_pk, NAME, SURNAME, login from KAT_OFFICERS, KAT_PERSONS where KAT_OFFICERS.OFFICER_PK = KAT_PERSONS.PERSON_PK AND KAT_PERSONS.PERSON_PK = ? ORDER BY SURNAME";
//           try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, personPK);
//               ResultSet rs = pstmt.executeQuery();
//            
//            while (rs.next()) {
//
// 
//                    personPK = rs.getInt(1);               
//                    label_jmeno.setText(rs.getString(2));
//                    label_prijmeni.setText(rs.getString(3));
//                    label_login.setText(rs.getString(4));
//                 }
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
                label_login.setText(rs.getString(4));         
        }
    }catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }
           
           zobrazPozemky();
           zobrazStavby();
           zjistiPKPozemku();
           zjistiPKStavby();
           label_pocetSchvalenych.setText(pocetSchvalenych+"");
    }
    
    public void zjistiPK(){
    String sql = "select person_pk, SURNAME from KAT_OFFICERS, KAT_PERSONS where KAT_OFFICERS.OFFICER_PK = KAT_PERSONS.PERSON_PK ORDER BY SURNAME";
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
//
//     //       listOsob.addElement(rs.getString(2) +" "+ rs.getString(3));
//                        }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
        
        try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){ 
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            if(rs.getRow() == index +1){
                    personPK = rs.getInt(1);               
            }               
        }
    }
    catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }
    }
    public void zjistiPKPozemku(){
        String sql = "select DISTINCT KAT_MAP_OBJECTS.OBJECT_PK\n" +
"FROM KAT_Officers, KAT_OWNERSHIP, KAT_MAP_OBJECTS\n" +
"WHERE OFFICER_PK = ? AND KAT_MAP_OBJECTS.TYPE = 0 AND KAT_Officers.OFFICER_PK = KAT_OWNERSHIP.OFFICER_FK AND KAT_OWNERSHIP.OBJECT_FK = KAT_MAP_OBJECTS.OBJECT_PK\n" +
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
"FROM KAT_Officers, KAT_OWNERSHIP, KAT_MAP_OBJECTS\n" +
"WHERE OFFICER_PK = ? AND KAT_MAP_OBJECTS.TYPE = 1 AND KAT_Officers.OFFICER_PK = KAT_OWNERSHIP.OFFICER_FK AND KAT_OWNERSHIP.OBJECT_FK = KAT_MAP_OBJECTS.OBJECT_PK\n" +
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
        
     String sql = "select DISTINCT KAT_MAP_OBJECTS.OBJECT_PK\n" +
"FROM KAT_Officers, KAT_OWNERSHIP, KAT_MAP_OBJECTS\n" +
"WHERE OFFICER_PK = ? AND KAT_MAP_OBJECTS.TYPE = 0 AND KAT_Officers.OFFICER_PK = KAT_OWNERSHIP.OFFICER_FK AND KAT_OWNERSHIP.OBJECT_FK = KAT_MAP_OBJECTS.OBJECT_PK\n" +
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
//                Btn_prejdiNaPozemek.setEnabled(true);;
//            }
//            while (rs.next()) {
//           
//                    listPozemku.addElement("ID pozemku: "+rs.getString(1));
//                    pocetSchvalenych++;
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
                Btn_prejdiNaPozemek.setEnabled(true);;
            }
        while (rs.next()) {
            listPozemku.addElement("ID pozemku: "+rs.getString(1));
            pocetSchvalenych++;               
        }
    }
    catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }
        
        List_seznamPozemku.setModel(listPozemku);
    }
    
    public void zobrazStavby(){
        listStaveb.clear();
        
     String sql = "select DISTINCT KAT_MAP_OBJECTS.OBJECT_PK\n" +
"FROM KAT_Officers, KAT_OWNERSHIP, KAT_MAP_OBJECTS\n" +
"WHERE OFFICER_PK = ? AND KAT_MAP_OBJECTS.TYPE = 1 AND KAT_Officers.OFFICER_PK = KAT_OWNERSHIP.OFFICER_FK AND KAT_OWNERSHIP.OBJECT_FK = KAT_MAP_OBJECTS.OBJECT_PK\n" +
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
//                    pocetSchvalenych++;
//                    listStaveb.addElement("ID stavby: "+rs.getString(1));
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
            pocetSchvalenych++;
            listStaveb.addElement("ID stavby: "+rs.getString(1));              
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
        List_seznamUredniku = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Btn_UpravitUrednika = new javax.swing.JButton();
        Btn_evidujUrednika = new javax.swing.JButton();
        Btn_OdstanitUrednika = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        label_pocetSchvalenych = new javax.swing.JLabel();
        label_login = new javax.swing.JLabel();
        label_prijmeni = new javax.swing.JLabel();
        label_jmeno = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        List_seznamPozemku = new javax.swing.JList<>();
        Btn_prejdiNaPozemek = new javax.swing.JButton();
        Btn_prejdiNaStavbu = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        List_seznamStaveb = new javax.swing.JList<>();
        jLabel9 = new javax.swing.JLabel();

        List_seznamUredniku.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        List_seznamUredniku.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        List_seznamUredniku.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                List_seznamUrednikuValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(List_seznamUredniku);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Jméno:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Příjmení:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Login:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Počet schválených");

        Btn_UpravitUrednika.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_UpravitUrednika.setText("Upravit Úředníka");
        Btn_UpravitUrednika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_UpravitUrednikaActionPerformed(evt);
            }
        });

        Btn_evidujUrednika.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_evidujUrednika.setText("Evidovat Úředníka");
        Btn_evidujUrednika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_evidujUrednikaActionPerformed(evt);
            }
        });

        Btn_OdstanitUrednika.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_OdstanitUrednika.setText("Odstranit Úředníka");
        Btn_OdstanitUrednika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_OdstanitUrednikaActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Seznam všech úředníků:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("Seznam schválených pozemků:");

        label_pocetSchvalenych.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_pocetSchvalenych.setText("0");

        label_login.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_login.setText("login");

        label_prijmeni.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_prijmeni.setText("Příjmení");

        label_jmeno.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_jmeno.setText("Jméno");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("nemovitostí:");

        List_seznamPozemku.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                List_seznamPozemkuValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(List_seznamPozemku);

        Btn_prejdiNaPozemek.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_prejdiNaPozemek.setText("přejít na pozemek");
        Btn_prejdiNaPozemek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_prejdiNaPozemekActionPerformed(evt);
            }
        });

        Btn_prejdiNaStavbu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_prejdiNaStavbu.setText("přejít na stavbu");
        Btn_prejdiNaStavbu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_prejdiNaStavbuActionPerformed(evt);
            }
        });

        List_seznamStaveb.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                List_seznamStavebValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(List_seznamStaveb);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setText("Seznam schválených staveb:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3))
                                .addGap(81, 81, 81)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label_login)
                                    .addComponent(label_prijmeni)
                                    .addComponent(label_jmeno)))
                            .addComponent(jLabel4)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_pocetSchvalenych))
                            .addComponent(jLabel7)
                            .addComponent(jLabel9)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Btn_prejdiNaPozemek))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Btn_prejdiNaStavbu))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Btn_evidujUrednika)
                        .addGap(37, 37, 37)
                        .addComponent(Btn_UpravitUrednika)
                        .addGap(63, 63, 63)
                        .addComponent(Btn_OdstanitUrednika)))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(9, 9, 9)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(label_jmeno)
                                .addGap(9, 9, 9)
                                .addComponent(label_prijmeni)))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(label_login))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(label_pocetSchvalenych))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(Btn_prejdiNaPozemek)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(146, 146, 146)
                                .addComponent(Btn_prejdiNaStavbu)))))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Btn_UpravitUrednika)
                    .addComponent(Btn_evidujUrednika)
                    .addComponent(Btn_OdstanitUrednika))
                .addContainerGap(31, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Při změně položky v jListu se zobrazí požadovaný úředník
     * @param evt 
     */
    private void List_seznamUrednikuValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_List_seznamUrednikuValueChanged
        index = List_seznamUredniku.getSelectedIndex();
        zjistiPK();
        zobrazUrednika();
    }//GEN-LAST:event_List_seznamUrednikuValueChanged
/**
 * Otevře se okno Vloz_urednika v režimu "editace"
 * @param evt 
 */
    private void Btn_UpravitUrednikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_UpravitUrednikaActionPerformed
          HlavniJFrame.okna.urednik = new Vloz_urednika1(HlavniJFrame.hlavniOkno, true);
          HlavniJFrame.okna.urednik.initForInsert(this, true, HlavniJFrame.hlavniOkno.okna.vsechnyOsoby);
          HlavniJFrame.okna.urednik.setVisible(true);
    }//GEN-LAST:event_Btn_UpravitUrednikaActionPerformed
/**
 * Otevře se okno Vloz_urednika v režimu tvorby nové osoby
 * @param evt 
 */
    private void Btn_evidujUrednikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_evidujUrednikaActionPerformed
          HlavniJFrame.okna.urednik = new Vloz_urednika1(HlavniJFrame.hlavniOkno, true);
          HlavniJFrame.okna.urednik.initForInsert(this, false, HlavniJFrame.hlavniOkno.okna.vsechnyOsoby);
          HlavniJFrame.okna.urednik.setVisible(true);
    }//GEN-LAST:event_Btn_evidujUrednikaActionPerformed

    /**
     * Zavolá dotaz pro odstranění záznamu z tabulky Úředníků (osoba zůstane)
     * @param evt 
     */
    private void Btn_OdstanitUrednikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_OdstanitUrednikaActionPerformed
        String sql = "DELETE FROM KAT_OFFICERS WHERE OFFICER_PK = ?";
//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//            PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, personPK);
//            ResultSet rs = pstmt.executeQuery();
//            conn.close();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
        try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){
        pstmt.setInt(1, personPK);
        pstmt.executeUpdate();
    }catch(SQLException SQLEx){
        System.err.println("SQLxception: " + SQLEx.getMessage());
    }
        naplnJListUredniku();
        zobrazUrednika();
    }//GEN-LAST:event_Btn_OdstanitUrednikaActionPerformed

    private void Btn_prejdiNaPozemekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_prejdiNaPozemekActionPerformed
      HlavniJFrame.hlavniOkno.okna.zobrazPozemek(pozemekPK);  // TODO add your handling code here:
    }//GEN-LAST:event_Btn_prejdiNaPozemekActionPerformed

    private void Btn_prejdiNaStavbuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_prejdiNaStavbuActionPerformed
      HlavniJFrame.hlavniOkno.okna.zobrazStavbu(stavbaPK);  // TODO add your handling code here:
    }//GEN-LAST:event_Btn_prejdiNaStavbuActionPerformed

    private void List_seznamPozemkuValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_List_seznamPozemkuValueChanged
        indexPozemku = List_seznamPozemku.getSelectedIndex() ; 
        zjistiPKPozemku();        // TODO add your handling code here:
    }//GEN-LAST:event_List_seznamPozemkuValueChanged

    private void List_seznamStavebValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_List_seznamStavebValueChanged
        indexStavby = List_seznamStaveb.getSelectedIndex() ; 
        zjistiPKStavby();
    }//GEN-LAST:event_List_seznamStavebValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Btn_OdstanitUrednika;
    private javax.swing.JButton Btn_UpravitUrednika;
    private javax.swing.JButton Btn_evidujUrednika;
    private javax.swing.JButton Btn_prejdiNaPozemek;
    private javax.swing.JButton Btn_prejdiNaStavbu;
    private javax.swing.JList<String> List_seznamPozemku;
    private javax.swing.JList<String> List_seznamStaveb;
    public javax.swing.JList<String> List_seznamUredniku;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    public javax.swing.JLabel label_jmeno;
    public javax.swing.JLabel label_login;
    public javax.swing.JLabel label_pocetSchvalenych;
    public javax.swing.JLabel label_prijmeni;
    // End of variables declaration//GEN-END:variables
}
