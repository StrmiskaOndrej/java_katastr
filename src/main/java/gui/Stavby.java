/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui_hlavni.HlavniJFrame;
import gui_okno.Vlastnictvi;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import oracle.jdbc.OracleResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.pool.OracleDataSource;
import oracle.ord.im.OrdImage;


/**
 *
 * @author strmiska
 */
public class Stavby extends javax.swing.JPanel {
    public int objectPK;
    public int personPK;
    public int officerPK;
    public int index = 0;
    public int indexVlastnika = 0;
    public float plocha;
    public DefaultListModel<String> listStaveb = new DefaultListModel<>();
    public DefaultListModel<String> listVlastniku = new DefaultListModel<>();
    /**
     * Creates new form Stavby
     */
    JLabel jlab = new JLabel();
    OrdImage imgProxy = null;
    public Stavby() {
        initComponents();
    }
    
    public void inicializuj() throws SQLException, IOException{
            zjistiPKObjektu();
            naplnJListStaveb();  
            zobrazStavbu();
            
    }
    
    public void naplnJListStaveb(){
        String sql = "select Object_pk, OID, Surface from KAT_MAP_OBJECTS where type = 1 AND VTO = future() ORDER BY OID";
        listStaveb.clear();
        List_seznamStaveb.setModel(listStaveb);

//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//            listStaveb.addElement(rs.getString(1));
//                        }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
    try (Statement stmt = HlavniJFrame.dtb.getConnection().createStatement()){ 
        try (ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                listStaveb.addElement(rs.getString(2));
            }
        }
    } catch (SQLException sqlEx) {
         System.err.println("SQLException (Search-loadShapesFromDb()): " + sqlEx.getMessage());
    }
        

        List_seznamStaveb.setModel(listStaveb);        
    }
    
    public void zobrazStavbu(){
//        label_id.setText(objectPK+"");
//        label_oid.setText(objectPK+"");
        listVlastniku.clear();
        
     String sql = "select KAT_PERSONS.PERSON_PK, KAT_persons.name,  KAT_persons.Surname,Kat_ownership.property_share, KAT_OFFICERS.OFFICER_PK, \n" +
"(select Kat_persons.name FROM KAT_PERSONS, KAT_OFFICERS WHERE KAT_PERSONS.PERSON_PK = KAT_OFFICERS.OFFICER_PK and KAT_OWNERSHIP.OFFICER_FK = KAT_OFFICERS.OFFICER_PK)UrednikName, \n" +
"(select Kat_persons.SURNAME FROM KAT_PERSONS, KAT_OFFICERS WHERE KAT_PERSONS.PERSON_PK = KAT_OFFICERS.OFFICER_PK and KAT_OWNERSHIP.OFFICER_FK = KAT_OFFICERS.OFFICER_PK)UrednikPrijmeni, KAT_MAP_OBJECTS.SURFACE\n" +
"FROM KAT_MAP_OBJECTS\n" +
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
                    
                    personPK = rs.getInt(1);               
                    officerPK = rs.getInt(5);   
                    
                    label_urednik.setText(rs.getString(6) + " " + rs.getString(7));
                    
                    listVlastniku.addElement(rs.getString(2) +" "+ rs.getString(3) + " " + (rs.getFloat(4))*100 + "%");

     
            }
        zobrazObrazek();
    }catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }catch (IOException ex) {
                Logger.getLogger(Stavby.class.getName()).log(Level.SEVERE, null, ex);
            }
    
//     try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//                pstmt.setInt(1, objectPK);
//                 
//            ResultSet rs = pstmt.executeQuery();
//            if (!rs.isBeforeFirst() ) {    
//                label_urednik.setText("Neschváleno");
//                Btn_prejdiNaOsobu.setEnabled(false);
//                Btn_prejdiNaUrednika.setEnabled(false);
//            } else{
//                Btn_prejdiNaOsobu.setEnabled(true);
//                Btn_prejdiNaUrednika.setEnabled(true);
//            }
//            while (rs.next()) {
//                    
//                    personPK = rs.getInt(1);               
//                    officerPK = rs.getInt(5);   
//                    
//                    label_urednik.setText(rs.getString(6) + " " + rs.getString(7));
//                    
//                    listVlastniku.addElement(rs.getString(2) +" "+ rs.getString(3) + " " + (rs.getFloat(4))*100 + "%");
//     
//            }
//            try {    
//                zobrazObrazek();
//            } catch (IOException ex) {
//                Logger.getLogger(Stavby.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
        
        indexVlastnika = 0;
        zjistiPKVlastnika();
        List_seznamVlastniku.setModel(listVlastniku);
    }
    
    public void zjistiPKObjektu(){
        String sql = "select Object_pk, OID, Surface from KAT_MAP_OBJECTS where type = 1 AND VTO = future() ORDER BY OID";
        
    try (Statement stmt = HlavniJFrame.dtb.getConnection().createStatement()){ 
        try (ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                if(rs.getRow() == index +1){
                    objectPK = rs.getInt(1); 
                    label_id.setText(objectPK+"");
                    label_oid.setText(rs.getInt(2)+"");
                    plocha = rs.getFloat(3);
                        if(plocha != 0){
                            label_vymera.setText(plocha+"");
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
    
    public void zjistiPKVlastnika(){
        String sql = "select KAT_PERSONS.PERSON_PK FROM KAT_MAP_OBJECTS\n" +
"INNER JOIN KAT_OWNERSHIP ON KAT_MAP_OBJECTS.object_pk = KAT_OWNERSHIP.OBJECT_FK\n" +
"INNER JOIN KAT_PERSONS ON KAT_OWNERSHIP.PERSON_FK = KAT_PERSONS.PERSON_PK\n" +
"WHERE KAT_MAP_OBJECTS.object_pk=?";
//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, objectPK);
//            ResultSet rs = pstmt.executeQuery();            
//            while (rs.next()) {
//               if(rs.getRow() == indexVlastnika +1){
//                    personPK = rs.getInt(1); 
//               }               
//            }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
    try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){ 
        pstmt.setInt(1, objectPK);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            if(rs.getRow() == indexVlastnika +1){
                personPK = rs.getInt(1); 
           }               
        }
    }
    catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }

        
    }
    
    public void smazFotku(){
    String sql = "delete from KAT_PICTURES WHERE OBJECT_FK = ?";
//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, objectPK);
//            
//            pstmt.executeUpdate();
//           
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
    try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){
        pstmt.setInt(1, objectPK);
        pstmt.executeUpdate();
    }catch(SQLException SQLEx){
        System.err.println("SQLxception: " + SQLEx.getMessage());
    }
    }
    
    public void nahrajFotku(){
        smazFotku();
    String sql = "insert into KAT_PICTURES(OBJECT_FK, PHOTO) values (?,ordsys.ordimage.init())";
//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, objectPK);
//            
//            pstmt.executeUpdate();
//           
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
    try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){
        pstmt.setInt(1, objectPK);
        pstmt.executeUpdate();
    }catch(SQLException SQLEx){
        System.err.println("SQLxception: " + SQLEx.getMessage());
    }
    }

    
    public void loadFotoFromFile(Connection connection, String filename) throws SQLException, IOException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try {
            OrdImage imgProxy = null;
            // ziskame proxy
            OraclePreparedStatement pstmtSelect = (OraclePreparedStatement) connection.prepareStatement(
                    "Select Photo from KAT_PICTURES WHERE OBJECT_FK = ? for update");
            try {
                pstmtSelect.setInt(1, objectPK);

                OracleResultSet rset = (OracleResultSet) pstmtSelect.executeQuery();
                try {
                    if (rset.next()) {
                        imgProxy = (OrdImage) rset.getORAData("photo", OrdImage.getORADataFactory());
                    }
                } finally {
                    rset.close();
                }
            } finally {
                pstmtSelect.close();
            }
            // pouzijeme proxy
            imgProxy.loadDataFromFile(filename);
            imgProxy.setProperties();
            // ulozime zmenene obrazky
            OraclePreparedStatement pstmtUpdate1 = (OraclePreparedStatement) connection.prepareStatement(
                    "Update KAT_PICTURES SET PHOTO = ? WHERE OBJECT_FK = ?");
            try {
                pstmtUpdate1.setORAData(1, imgProxy);
                pstmtUpdate1.setInt(2, objectPK);
                pstmtUpdate1.executeUpdate();
            } finally {
                pstmtUpdate1.close();
            }
            PreparedStatement pstmtUpdate2 = connection.prepareStatement(
                    "Update KAT_PICTURES v SET v.PHOTO_SI = SI_StillImage(v.PHOTO.getContent()) WHERE v.OBJECT_FK = ?");
            try {
                pstmtUpdate2.setInt(1, objectPK);
                pstmtUpdate2.executeUpdate();
            } finally {
                pstmtUpdate2.close();
            }
            
            PreparedStatement pstmtUpdate3 = connection.prepareStatement(
                    "update KAT_PICTURES set "
                    + "photo_ac = SI_AverageColor(photo_si), "
                    + "photo_ch = SI_ColorHistogram(photo_si), "
                    + "photo_pc = SI_PositionalColor(photo_si), "
                    + "photo_tx = SI_Texture(photo_si) "
                    + "WHERE OBJECT_FK = ?");
            try {
                pstmtUpdate3.setInt(1, objectPK);
                pstmtUpdate3.executeUpdate();
            } finally {
                pstmtUpdate3.close();
            }
            
            System.out.println("Obrázek nahrán");
            connection.commit();
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }
    
    public void listFilesForFolder(final File folder) {
    for (final File fileEntry : folder.listFiles()) {
        if (fileEntry.isDirectory()) {
            listFilesForFolder(fileEntry);
        } else {
            System.out.println(fileEntry.getName());
        }
    }
}

    public void zobrazObrazek() throws IOException{
        String sql ="SELECT PICTURE_PK, Photo FROM KAT_PICTURES WHERE OBJECT_FK = ?";
        
    try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){ 
        pstmt.setInt(1, objectPK);
        OracleResultSet rs = (OracleResultSet) pstmt.executeQuery();
        ImageIcon imageIcon;
        while (rs.next()) {
                OrdImage img = (OrdImage) rs.getORAData("Photo", OrdImage.getORADataFactory());
                byte[] tmp = img.getDataInByteArray();  
                if(img.getHeight() == 0 || img.getWidth() ==0){
                    imageIcon = new ImageIcon("./noImage.jpg");
                }else{
                    imageIcon = new ImageIcon (tmp);   
                }
                Image image = imageIcon.getImage(); // transform it 
                Image newimg = image.getScaledInstance(190, 190,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                imageIcon = new ImageIcon(newimg);
              
                jlab.setIcon(imageIcon);
              
                jlab.setHorizontalAlignment(JLabel.CENTER);
                jSP.getViewport().add(jlab);
                
            }
    }
    catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }
        
//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement("SELECT PICTURE_PK, Photo FROM KAT_PICTURES WHERE OBJECT_FK = ?")) {
//            pstmt.setInt(1, objectPK);
//            OracleResultSet rs = (OracleResultSet) pstmt.executeQuery();
//            ImageIcon imageIcon;
//            while (rs.next()) {
//                OrdImage img = (OrdImage) rs.getORAData("Photo", OrdImage.getORADataFactory());
//                byte[] tmp = img.getDataInByteArray();  
//                if(img.getHeight() == 0 || img.getWidth() ==0){
//                    imageIcon = new ImageIcon("./noImage.jpg");
//                }else{
//                    imageIcon = new ImageIcon (tmp);   
//                }
//                Image image = imageIcon.getImage(); // transform it 
//                Image newimg = image.getScaledInstance(190, 190,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
//                imageIcon = new ImageIcon(newimg);
//              
//                jlab.setIcon(imageIcon);
//              
//                jlab.setHorizontalAlignment(JLabel.CENTER);
//                jSP.getViewport().add(jlab);
//                
//            }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }

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
        List_seznamStaveb = new javax.swing.JList<>();
        jLabel9 = new javax.swing.JLabel();
        label_id = new javax.swing.JLabel();
        label_oid = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        label_vymera = new javax.swing.JLabel();
        label_urednik = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        Btn_prejdiNaUrednika = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        List_seznamVlastniku = new javax.swing.JList<>();
        Btn_prejdiNaOsobu = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        Btn_nahratFotku = new javax.swing.JButton();
        jSP = new javax.swing.JScrollPane();
        Btn_smazatFotku = new javax.swing.JButton();
        Btn_najitStavbu = new javax.swing.JButton();
        Btn_prejdiNaVlastnictvi = new javax.swing.JButton();
        Btn_vypoctiPlochu = new javax.swing.JButton();

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("Seznam staveb:");

        List_seznamStaveb.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                List_seznamStavebValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(List_seznamStaveb);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setText("ID stavby:");

        label_id.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_id.setText("ID");

        label_oid.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_oid.setText("OID");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel10.setText("OID stavby:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText("Výměra:");

        label_vymera.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_vymera.setText("plocha zatím nevypočítána");

        label_urednik.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_urednik.setText("Úředník");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel15.setText("Pozemek schválil:");

        Btn_prejdiNaUrednika.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_prejdiNaUrednika.setText("Přejít na úředníka");
        Btn_prejdiNaUrednika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_prejdiNaUrednikaActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setText("Seznam vlastníků:");

        List_seznamVlastniku.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                List_seznamVlastnikuValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(List_seznamVlastniku);

        Btn_prejdiNaOsobu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_prejdiNaOsobu.setText("Přejít na osobu");
        Btn_prejdiNaOsobu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_prejdiNaOsobuActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton10.setText("Zobrazit na mapě");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        Btn_nahratFotku.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_nahratFotku.setText("Nahrát fotku");
        Btn_nahratFotku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_nahratFotkuActionPerformed(evt);
            }
        });

        Btn_smazatFotku.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_smazatFotku.setText("Smazat fotku");
        Btn_smazatFotku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_smazatFotkuActionPerformed(evt);
            }
        });

        Btn_najitStavbu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_najitStavbu.setText("Najít podobnou stavbu");
        Btn_najitStavbu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_najitStavbuActionPerformed(evt);
            }
        });

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
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel9))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(label_id)
                                            .addComponent(label_oid))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton10))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(label_vymera)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                                        .addComponent(Btn_vypoctiPlochu))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addGap(18, 18, 18)
                                        .addComponent(label_urednik))
                                    .addComponent(Btn_prejdiNaUrednika)
                                    .addComponent(jLabel13)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(Btn_prejdiNaOsobu)
                                            .addComponent(Btn_prejdiNaVlastnictvi))))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Btn_najitStavbu))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Btn_smazatFotku)
                                    .addComponent(Btn_nahratFotku))))
                        .addGap(24, 24, 24)
                        .addComponent(jSP, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(label_id))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(label_oid))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(label_vymera)
                                    .addComponent(Btn_vypoctiPlochu)))
                            .addComponent(jButton10))
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(label_urednik))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Btn_prejdiNaUrednika)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Btn_prejdiNaOsobu)
                                .addGap(3, 3, 3)
                                .addComponent(Btn_prejdiNaVlastnictvi)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(Btn_nahratFotku)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Btn_smazatFotku)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Btn_najitStavbu)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSP, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void List_seznamStavebValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_List_seznamStavebValueChanged
        index = List_seznamStaveb.getSelectedIndex();
        zjistiPKObjektu();
        zobrazStavbu();
    }//GEN-LAST:event_List_seznamStavebValueChanged

    private void Btn_prejdiNaUrednikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_prejdiNaUrednikaActionPerformed
        HlavniJFrame.hlavniOkno.okna.zobrazUrednika(officerPK);        // TODO add your handling code here:
    }//GEN-LAST:event_Btn_prejdiNaUrednikaActionPerformed

    private void Btn_prejdiNaOsobuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_prejdiNaOsobuActionPerformed
        HlavniJFrame.hlavniOkno.okna.zobrazVlastnika(personPK);        // TODO add your handling code here:
    }//GEN-LAST:event_Btn_prejdiNaOsobuActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void List_seznamVlastnikuValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_List_seznamVlastnikuValueChanged
        indexVlastnika = List_seznamVlastniku.getSelectedIndex() ; 
        zjistiPKVlastnika();        // TODO add your handling code here:
    }//GEN-LAST:event_List_seznamVlastnikuValueChanged

    private void Btn_nahratFotkuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_nahratFotkuActionPerformed
        nahrajFotku();
        JFileChooser jfc = new JFileChooser();
        FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        jfc.setFileFilter(imageFilter);
        String cesta;
        
        if(jfc.showOpenDialog(Btn_smazatFotku) == JFileChooser.APPROVE_OPTION){
              java.io.File f = jfc.getSelectedFile();
              cesta = f.toString();
        
            try {
                zobrazObrazek();
            } catch (IOException ex) {
                Logger.getLogger(Stavby.class.getName()).log(Level.SEVERE, null, ex);
            }
                 
                
        }     
    }//GEN-LAST:event_Btn_nahratFotkuActionPerformed

    private void Btn_smazatFotkuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_smazatFotkuActionPerformed
        smazFotku();
    }//GEN-LAST:event_Btn_smazatFotkuActionPerformed

    private void Btn_najitStavbuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_najitStavbuActionPerformed
       int tmp = 0;
       String sql ="SELECT src.OBJECT_FK as source, dst.Object_fk as destination, SI_ScoreByFtrList(new SI_FeatureList\n" +
"                (src.photo_ac,0.3,src.photo_ch,0.3,src.photo_pc,0.1,src.photo_tx,0.3),dst.photo_si)\n" +
"                as similarity FROM KAT_PICTURES src, KAT_PICTURES dst WHERE (src.Object_fk <> dst.Object_fk) \n" +
"                AND src.Object_fk = ? ORDER BY similarity ASC";
//        try (Connection conn = HlavniJFrame.dtb.pripoj();
//                
//                PreparedStatement pstmt = conn.prepareStatement("SELECT src.OBJECT_FK as source, dst.Object_fk as destination, SI_ScoreByFtrList(new SI_FeatureList\n" +
//"                (src.photo_ac,0.3,src.photo_ch,0.3,src.photo_pc,0.1,src.photo_tx,0.3),dst.photo_si)\n" +
//"                as similarity FROM KAT_PICTURES src, KAT_PICTURES dst WHERE (src.Object_fk <> dst.Object_fk) \n" +
//"                AND src.Object_fk = ? ORDER BY similarity ASC")) {
//            pstmt.setInt(1, objectPK);
//            ResultSet rset = pstmt.executeQuery();
//            if (rset.next()) {
//                    tmp = rset.getInt(2);                    
//                }
//        conn.close();    
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
        try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){ 
        pstmt.setInt(1, objectPK);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            tmp = rs.getInt(2);             
        }
    }
    catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }
        if(tmp != 0){
            objectPK = tmp;
            zobrazStavbu();
        }
    }//GEN-LAST:event_Btn_najitStavbuActionPerformed

    private void Btn_prejdiNaVlastnictviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_prejdiNaVlastnictviActionPerformed
          HlavniJFrame.okna.vlastnictvi = new Vlastnictvi(HlavniJFrame.hlavniOkno, true);
          HlavniJFrame.okna.vlastnictvi.initForInsertStavby(this, objectPK);
          HlavniJFrame.okna.vlastnictvi.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_Btn_prejdiNaVlastnictviActionPerformed

    private void Btn_vypoctiPlochuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_vypoctiPlochuActionPerformed
            label_vymera.setText(logika.Dotazy.vypoctiPlochu(objectPK)+"");
            Btn_vypoctiPlochu.setEnabled(false);
    }//GEN-LAST:event_Btn_vypoctiPlochuActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Btn_nahratFotku;
    private javax.swing.JButton Btn_najitStavbu;
    private javax.swing.JButton Btn_prejdiNaOsobu;
    private javax.swing.JButton Btn_prejdiNaUrednika;
    private javax.swing.JButton Btn_prejdiNaVlastnictvi;
    private javax.swing.JButton Btn_smazatFotku;
    private javax.swing.JButton Btn_vypoctiPlochu;
    private javax.swing.JList<String> List_seznamStaveb;
    private javax.swing.JList<String> List_seznamVlastniku;
    private javax.swing.JButton jButton10;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jSP;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel label_id;
    private javax.swing.JLabel label_oid;
    private javax.swing.JLabel label_urednik;
    private javax.swing.JLabel label_vymera;
    // End of variables declaration//GEN-END:variables
}
