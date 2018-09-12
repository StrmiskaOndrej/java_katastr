/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui_okno;

import gui_hlavni.HlavniJFrame;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListModel;

/**
 *
 * @author strmiska
 */
public class Vloz_vlastnika extends javax.swing.JDialog {
    private gui_okno.Vlastnictvi parent;
    int vybranyPodil = 0;
    int objectPK;
    int personPK;
    float podil = 0;
    int indexOsoby =-1;
    boolean editace;
    public DefaultListModel<String> listNevlastniku = new DefaultListModel<>();
    /**
     * Creates new form Vloz_vlastnika
     */
    public Vloz_vlastnika(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public void initForInsert(Vlastnictvi parent, int objectID, boolean edit)
	{
              this.parent = parent;
              objectPK = objectID;             
              
              if(!edit){
              editace = false;
              naplnOsoby();
              Btn_ulozVlastnika.setEnabled(false);
              slider_podil.setMaximum(parent.zbyvajiciPodil); 
              
              }else{
                editace = true;
                slider_podil.setMaximum(parent.zbyvajiciPodil + parent.vlastnikSPodilem.podil); 
                personPK = parent.vlastnikSPodilem.personPK;
                slider_podil.setValue(parent.vlastnikSPodilem.podil);
                Btn_ulozVlastnika.setEnabled(true);
                listNevlastniku.addElement(parent.vlastnikSPodilem.jmeno +" "+ parent.vlastnikSPodilem.prijmeni);
                List_seznamNevlastniku.setModel(listNevlastniku);
                indexOsoby =1;
                List_seznamNevlastniku.setSelectedIndex(1);
              }
        }
    public void naplnOsoby(){
        String sql = "select KAT_PERSONS.PERSON_PK, KAT_persons.name, KAT_persons.Surname\n" +
"FROM KAT_PERSONS WHERE PERSON_PK NOT IN \n" +
"(select KAT_PERSONS.PERSON_PK\n" +
"FROM KAT_MAP_OBJECTS\n" +
"INNER JOIN KAT_OWNERSHIP ON KAT_MAP_OBJECTS.object_pk = KAT_OWNERSHIP.OBJECT_FK\n" +
"INNER JOIN KAT_PERSONS ON KAT_OWNERSHIP.PERSON_FK = KAT_PERSONS.PERSON_PK\n" +
"WHERE OBJECT_PK = ?) Order by surname";
        try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){ 
         pstmt.setInt(1, objectPK);
         ResultSet rs = pstmt.executeQuery();
         while (rs.next()) {  
             listNevlastniku.addElement(rs.getString(2) +" "+ rs.getString(3));
             }
     }
    catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }
    List_seznamNevlastniku.setModel(listNevlastniku);
        
        
    }
    
    public void zjistiPK(){
        String sql = "select KAT_PERSONS.PERSON_PK, KAT_persons.name, KAT_persons.Surname\n" +
"FROM KAT_PERSONS WHERE PERSON_PK NOT IN \n" +
"(select KAT_PERSONS.PERSON_PK\n" +
"FROM KAT_MAP_OBJECTS\n" +
"INNER JOIN KAT_OWNERSHIP ON KAT_MAP_OBJECTS.object_pk = KAT_OWNERSHIP.OBJECT_FK\n" +
"INNER JOIN KAT_PERSONS ON KAT_OWNERSHIP.PERSON_FK = KAT_PERSONS.PERSON_PK\n" +
"WHERE OBJECT_PK = ?) Order by surname";
    try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){ 
         pstmt.setInt(1, objectPK);
         ResultSet rs = pstmt.executeQuery();
         while (rs.next()) {  
             if(rs.getRow() == indexOsoby +1){
                personPK = rs.getInt(1); 
             }
         }
     }
    catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }
    }
    
    public void pridejVlastnika(){
        String sql = "INSERT INTO KAT_OWNERSHIP (PERSON_FK, OFFICER_FK, OBJECT_FK, PROPERTY_SHARE, VFROM, VTO) VALUES (?,?,?,?,? , timestamp '9999-12-31 23:59:59')";
    try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){
        pstmt.setInt(1, personPK);
        pstmt.setInt(2, parent.officerPK);
        pstmt.setInt(3, parent.objectPK);
        pstmt.setFloat(4, podil);
        pstmt.setTimestamp(5, logika.Datum.prevedNaTimestamp(parent.zacatekVlastnictvi));
        pstmt.executeUpdate();
    }catch(SQLException SQLEx){
        System.err.println("SQLxception: " + SQLEx.getMessage());
    }
    }
    
    public void upravVlastnika(){
        String sql = "update KAT_OWNERSHIP set PROPERTY_SHARE = ? where OBJECT_FK = ? AND PERSON_FK = ?";
    try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){
        pstmt.setFloat(1, podil);
        pstmt.setInt(2, parent.objectPK);
        pstmt.setInt(3, parent.vlastnikSPodilem.personPK);
        pstmt.executeUpdate();
    }catch(SQLException SQLEx){
        System.err.println("SQLxception: " + SQLEx.getMessage());
    }
    }
    
    private static java.sql.Timestamp getCurrentTimeStamp() {

	java.util.Date today = new java.util.Date();
	return new java.sql.Timestamp(today.getTime());
        

}
    public void aktivaceButtonu(){
        if(vybranyPodil == 0 || indexOsoby ==-1){
            Btn_ulozVlastnika.setEnabled(false);
        }else{
            Btn_ulozVlastnika.setEnabled(true);
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
        List_seznamNevlastniku = new javax.swing.JList<>();
        jLabel6 = new javax.swing.JLabel();
        slider_podil = new javax.swing.JSlider();
        label_urednik1 = new javax.swing.JLabel();
        label_vybranyPodil = new javax.swing.JLabel();
        Btn_ulozVlastnika = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        List_seznamNevlastniku.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        List_seznamNevlastniku.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        List_seznamNevlastniku.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                List_seznamNevlastnikuValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(List_seznamNevlastniku);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Seznam osob:");

        slider_podil.setOrientation(javax.swing.JSlider.VERTICAL);
        slider_podil.setPaintTicks(true);
        slider_podil.setToolTipText("Nastav podíl vlastnictví");
        slider_podil.setValue(0);
        slider_podil.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slider_podilStateChanged(evt);
            }
        });

        label_urednik1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_urednik1.setText("Podíl:");

        label_vybranyPodil.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_vybranyPodil.setText("0%");

        Btn_ulozVlastnika.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Btn_ulozVlastnika.setText("Uložit Vlastníka");
        Btn_ulozVlastnika.setEnabled(false);
        Btn_ulozVlastnika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_ulozVlastnikaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(53, 53, 53)
                                        .addComponent(label_urednik1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(label_vybranyPodil))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(76, 76, 76)
                                        .addComponent(slider_podil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(Btn_ulozVlastnika)))
                .addContainerGap(170, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label_urednik1)
                            .addComponent(label_vybranyPodil))
                        .addGap(26, 26, 26)
                        .addComponent(slider_podil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(Btn_ulozVlastnika)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void List_seznamNevlastnikuValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_List_seznamNevlastnikuValueChanged
          indexOsoby = List_seznamNevlastniku.getSelectedIndex(); 
          aktivaceButtonu();
    }//GEN-LAST:event_List_seznamNevlastnikuValueChanged

    private void slider_podilStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slider_podilStateChanged
        vybranyPodil = slider_podil.getValue();        // TODO add your handling code here:
        label_vybranyPodil.setText(vybranyPodil+"%");
        aktivaceButtonu();
        
    }//GEN-LAST:event_slider_podilStateChanged

    private void Btn_ulozVlastnikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_ulozVlastnikaActionPerformed
    podil = (float)vybranyPodil/100;    
    if(!editace){    
        if(indexOsoby != -1){
            if(vybranyPodil != 0){
                zjistiPK();
                pridejVlastnika();
            }else{
                System.out.println("Nenastaven podíl"); 
            }
        }
        else{
            System.out.println("Nejprve vyberte osobu"); 
        }
    }else{
    if(vybranyPodil != 0){
                upravVlastnika();
            }else{
                System.out.println("Nenastaven podíl"); 
            }
    }
    parent.naplnUdaje();
    if(parent.parentPozemky != null){                
            parent.parentPozemky.zobrazPozemek();
        }
    if(parent.parentStavby != null){                
            parent.parentStavby.zobrazStavbu();
    }
    parent.vlastnikSPodilem = logika.Dotazy.zjistiUdajeVlastnika(parent.objectPK, parent.indexVlastnika);
    this.dispose(); 
    }//GEN-LAST:event_Btn_ulozVlastnikaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Vloz_vlastnika.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Vloz_vlastnika.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Vloz_vlastnika.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Vloz_vlastnika.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Vloz_vlastnika dialog = new Vloz_vlastnika(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Btn_ulozVlastnika;
    public javax.swing.JList<String> List_seznamNevlastniku;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label_urednik1;
    private javax.swing.JLabel label_vybranyPodil;
    private javax.swing.JSlider slider_podil;
    // End of variables declaration//GEN-END:variables
}
