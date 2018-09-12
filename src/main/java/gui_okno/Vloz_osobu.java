/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui_okno;

import gui_hlavni.HlavniJFrame;
import gui.VsechnyOsoby;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author strmiska
 */
public class Vloz_osobu extends javax.swing.JDialog {
private gui.VsechnyOsoby parent;
public boolean editace;

    /**
     * Creates new form Vloz_osobu3
     */
    public Vloz_osobu(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
    }
    
/**
 * Metoda spuštěná krátce po otevření tohoto okna, inicializuje rodiče objektu a nastavuje, zda okno slouží pro evidenci nového uživatele, nebo pro jeho úpravu, Pokud se jedná o úpravu uživatele, předvyplní údaje osoby
 * @param vso
 * @param edit 
 */
    public void initForInsert(VsechnyOsoby vso, Boolean edit)
	{
		this.parent = vso;
                this.editace = edit;
	//	jOsoba = new JOsoba();	
            if(edit){
            TF_jmeno.setText(parent.label_jmeno.getText());
            TF_prijmeni.setText((parent.label_prijmeni.getText()));
            TF_kontakt.setText(parent.label_kontakt.getText());
            TF_RC.setText(parent.label_RC.getText());
            TF_datNar.setText(parent.label_DN.getText());
            if(parent.label_pohlavi.getText().equals("Muž")){
                System.out.println("Muž");
                CB_pohlavi.setSelectedIndex(0);
            }else if(parent.label_pohlavi.getText().equals("Žena")){
                CB_pohlavi.setSelectedIndex(1);
            
            }else{
                CB_pohlavi.setSelectedIndex(2);
            }
	}
       }
    
    /**
     * Při uložení osoby nastavuje, na kterém indexu se osoba nachází
     */
    public void poziceNoveOsoby(){
    String sql = "select * from KAT_PERSONS ORDER BY SURNAME";
    try (Statement stmt = HlavniJFrame.dtb.getConnection().createStatement())
            { 
                try (ResultSet rs = stmt.executeQuery(sql))
                {
                    while (rs.next()) {
                        if(rs.getString(7).equals(TF_RC.getText())){
                        parent.index = rs.getRow() - 1;
                        }
                    }
                }
    } catch (SQLException sqlEx) {
                    System.err.println("SQLException (Search-loadShapesFromDb()): " + sqlEx.getMessage());
    }
    
//    try (Connection conn = HlavniJFrame.dtb.pripoj();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            ResultSet rs = pstmt.executeQuery();
//            
//            while (rs.next()) {
//
//               if(rs.getString(7).equals(TF_RC.getText())){
//                    parent.index = rs.getRow() - 1;
//
//               }
//               
//            }               
//                conn.close();
//     //       listOsob.addElement(rs.getString(2) +" "+ rs.getString(3));
//                        }
//            
//         catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
    }
    
    /**
     * Zavolá příkaz pro přidání osoby do DB, podle vyplněných údajů
     */
     public void pridejOsobuDoDB(){
        String sql = "INSERT INTO KAT_PERSONS (NAME, SURNAME, CONTACT, BIRTH, BIRTH_ID, SEX) VALUES (?, ?, ?, TO_DATE('1992-01-30 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), ?, ?)";

//        try (Statement stmt = HlavniJFrame.dtb.getConnection().createStatement())
//            { 
//                try (ResultSet rs = stmt.executeQuery(sql))
//                {
//                    while (rs.next()) {
//                        if(rs.getString(7).equals(TF_RC.getText())){
//                        parent.index = rs.getRow() - 1;
//                        }
//                    }
//                }
//            } catch (SQLException sqlEx) {
//                    System.err.println("SQLException (Search-loadShapesFromDb()): " + sqlEx.getMessage());
//            }
        
        try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql))
		{
                pstmt.setString(1, TF_jmeno.getText());
                pstmt.setString(2, TF_prijmeni.getText());
                pstmt.setString(3, TF_kontakt.getText());
                pstmt.setString(4, TF_RC.getText());
                pstmt.setInt(5, pohlaviNaInt(CB_pohlavi.getSelectedItem().toString()));
                pstmt.executeUpdate();
	}catch(SQLException SQLEx)
        {
            System.err.println("SQLxception: " + SQLEx.getMessage());
	}

   }
     
     /**
      * Zavolá příkaz pro úpravu osoby v DB, podle vyplěných údajů
      */
     public void upravOsobuVDB(){
    String sql = "update KAT_PERSONS set NAME = ? , SURNAME = ? , CONTACT = ? , BIRTH_ID = ? , SEX = ? where PERSON_PK = ?";
   //    try (Connection conn = HlavniJFrame.dtb.pripoj(); 
//               PreparedStatement pstmt = conn.prepareStatement(query)){
//        pstmt.setString(1, TF_jmeno.getText());     
//        pstmt.setString(2, TF_prijmeni.getText());
//        pstmt.setString(3, TF_kontakt.getText());
//        pstmt.setString(4, TF_RC.getText());
//        pstmt.setInt   (5, pohlaviNaInt(CB_pohlavi.getSelectedItem().toString()));
//        pstmt.setInt(6, parent.personPK);
//      // execute the java preparedstatement
//        pstmt.executeUpdate();      
//        conn.close();       
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } 
       
       try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql))
		{
                pstmt.setString(1, TF_jmeno.getText());     
        pstmt.setString(2, TF_prijmeni.getText());
        pstmt.setString(3, TF_kontakt.getText());
        pstmt.setString(4, TF_RC.getText());
        pstmt.setInt   (5, pohlaviNaInt(CB_pohlavi.getSelectedItem().toString()));
        pstmt.setInt(6, parent.personPK);
        pstmt.executeUpdate();
	}catch(SQLException SQLEx)
        {
            System.err.println("SQLxception: " + SQLEx.getMessage());
	}

   
   }
    
     /**
      * Metoda sloužící k převedení pohlaví osoby z typu int na String
      * @param poh
      * @return 
      */
    public static String pohlaviNaString(int poh){
      if(poh == 0){
        return "Muž";
      }else if(poh == 1){
      return "Žena";
      }else{
          return "Meuvedeno";
      }
    } 
    
    /**
     * Metoda sloužící k převedení pohlaví osoby z typu String na int
     * @param poh
     * @return 
     */
    public static int pohlaviNaInt(String poh){
            if(poh.equals("Muž")){
            return 0;
        }else if(poh.equals("Žena")){
            return 1;
        }else{
            return 2;
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

        jPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        TF_jmeno = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        TF_prijmeni = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        TF_RC = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TF_datNar = new javax.swing.JTextField();
        B_ulozitOsobu = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        CB_pohlavi = new javax.swing.JComboBox<>();
        TF_kontakt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel8.setFont(new java.awt.Font("Noto Sans", 1, 18)); // NOI18N
        jLabel8.setText("Vložení/úprava osoby");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Jméno:");

        TF_jmeno.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        TF_jmeno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_jmenoActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Přijmení:");

        TF_prijmeni.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        TF_prijmeni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_prijmeniActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Rodné číslo:");

        TF_RC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        TF_RC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_RCActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("Datum narození:");

        TF_datNar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        TF_datNar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_datNarActionPerformed(evt);
            }
        });

        B_ulozitOsobu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        B_ulozitOsobu.setText("Uložit osobu");
        B_ulozitOsobu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_ulozitOsobuActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Pohlaví:");

        CB_pohlavi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Muž", "Žena", "Neuvedeno" }));
        CB_pohlavi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CB_pohlaviActionPerformed(evt);
            }
        });

        TF_kontakt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        TF_kontakt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_kontaktActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("Kontakt:");

        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addGap(208, 208, 208)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7))
                        .addGap(33, 33, 33)
                        .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TF_prijmeni, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TF_jmeno, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TF_kontakt, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TF_RC, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TF_datNar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CB_pohlavi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addGap(234, 234, 234)
                        .addComponent(B_ulozitOsobu)))
                .addContainerGap(183, Short.MAX_VALUE))
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7))
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addComponent(TF_jmeno, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TF_prijmeni, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TF_kontakt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(TF_RC, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(TF_datNar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addComponent(CB_pohlavi)
                        .addGap(2, 2, 2))
                    .addComponent(jLabel6))
                .addGap(97, 97, 97)
                .addComponent(B_ulozitOsobu)
                .addGap(54, 54, 54))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 680, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 517, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TF_prijmeniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_prijmeniActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_prijmeniActionPerformed

    private void TF_jmenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_jmenoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_jmenoActionPerformed

    private void TF_RCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_RCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_RCActionPerformed

    private void TF_datNarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_datNarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_datNarActionPerformed
/**
 * Funkce vyvolaná po stisknutí tlačítka uložit, zavolá příkaz pro úpravu v DB, provede operace v rodičovském okně a zavře okno
 * @param evt 
 */
    private void B_ulozitOsobuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_ulozitOsobuActionPerformed
        if(!editace){
        pridejOsobuDoDB();
        } else{
        upravOsobuVDB();
        }    
        parent.naplnJListOsob();
        poziceNoveOsoby();
        parent.List_seznamOsob.setSelectedIndex(parent.index);
        parent.zobrazOsobu();
        this.dispose();	        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_B_ulozitOsobuActionPerformed

    private void CB_pohlaviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CB_pohlaviActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CB_pohlaviActionPerformed

    private void TF_kontaktActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_kontaktActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_kontaktActionPerformed

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
            java.util.logging.Logger.getLogger(Vloz_osobu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Vloz_osobu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Vloz_osobu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Vloz_osobu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Vloz_osobu dialog = new Vloz_osobu(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton B_ulozitOsobu;
    private javax.swing.JComboBox<String> CB_pohlavi;
    private javax.swing.JTextField TF_RC;
    private javax.swing.JTextField TF_datNar;
    private javax.swing.JTextField TF_jmeno;
    private javax.swing.JTextField TF_kontakt;
    private javax.swing.JTextField TF_prijmeni;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel;
    // End of variables declaration//GEN-END:variables
}
