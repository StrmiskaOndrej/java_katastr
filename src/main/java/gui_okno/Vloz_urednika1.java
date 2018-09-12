/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui_okno;

import gui_hlavni.HlavniJFrame;
import gui.Urednici;
import gui.VsechnyOsoby;
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
public class Vloz_urednika1 extends javax.swing.JDialog {
    private gui.Urednici parent;
    private gui.VsechnyOsoby vsechnyOsoby;
    public boolean editace;
    public int index = -1;
    public int personPK;
    public DefaultListModel<String> listNeUredniku = new DefaultListModel<>();
    /**
     * Creates new form vloz_urednika
     */
    public Vloz_urednika1(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        
        
    }
    
    /**
     * Funkce zavolána z rodičovského objektu (Urednici.java), nastaví někde proměnné, 
     * pokud se jedná o režim editace, tak předvyplní login, zároveň naplní a deaktivuje JList.
     * Pokud se jedná o tvorbu nové osoby, tak naplní jList
     * @param ure
     * @param edit
     * @param vse 
     */
public void initForInsert(Urednici ure, Boolean edit, VsechnyOsoby vse)
	{
            this.vsechnyOsoby = vse;
            this.parent = ure;
            this.editace = edit;         
           
            
            if(editace){
                List_seznamOsob.setModel(parent.listUredniku);
                List_seznamOsob.setSelectedIndex(parent.index);
                List_seznamOsob.disable();
                TF_login.setText(parent.label_login.getText());
            }else{
                naplnNeuredniky();
                
            }     
        }

/**
 * Zavolá dotaz v databázi a vypíše seznam všech osob, které jsou v tabulce osob, ale nejsou v tabulce úředníků.
 * Z tohoto dotazu naplní list.
 */
public void naplnNeuredniky(){
    
    String sql = "select NAME, SURNAME from KAT_PERSONS WHERE PERSON_PK NOT IN (SELECT OFFICER_PK FROM KAT_OFFICERS) ORDER BY SURNAME";
        listNeUredniku.clear();
        List_seznamOsob.setModel(listNeUredniku);
        try (Statement stmt = HlavniJFrame.dtb.getConnection().createStatement())
            { 
                try (ResultSet rs = stmt.executeQuery(sql))
                {
                    while (rs.next()) {
                        listNeUredniku.addElement(rs.getString(1) +" "+ rs.getString(2));  
                    }
                }
    } catch (SQLException sqlEx) {
                    System.err.println("SQLException (Search-loadShapesFromDb()): " + sqlEx.getMessage());
    }
    

        List_seznamOsob.setModel(listNeUredniku);
        
}

/**
 * Funkce sloužící pro zavolání dotazu na přidání účedníka do DB na základě vyplněných údajů
 */
public void pridejUrednikaDoDB(){
        zjistiPKOsoby();
        
        String sql = "INSERT INTO KAT_OFFICERS (OFFICER_PK, LOGIN, PASSWD, EMPLOYED_AS) VALUES (?, ?, ?, '1')";

        try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql))
		{
                pstmt.setInt(1, personPK);
            pstmt.setString(2, TF_login.getText());
            pstmt.setString(3, TF_heslo.getText());
                pstmt.executeUpdate();
	}catch(SQLException SQLEx)
        {
            System.err.println("SQLxception: " + SQLEx.getMessage());
	}

   }

/**
 * Funkce sloužící pro zavolání dotazu na úpravu položky v tabulce úředníků na základě vyplněných údajů
 */
public void upravUrednikaVDB(){
  
    String sql = "update KAT_OFFICERS set LOGIN = ? , PASSWD = ? where OFFICER_PK = ?";
       try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql))
		{
        pstmt.setString(1, TF_login.getText());     
        pstmt.setString(2, TF_heslo.getText());
        pstmt.setInt(3, parent.personPK);
        pstmt.executeUpdate();
	}catch(SQLException SQLEx)
        {
            System.err.println("SQLxception: " + SQLEx.getMessage());
        } 
   }

/**
 * Funcke pro zjištění PK vybrané osoby
 */
public void zjistiPKOsoby(){
        
     String sql = "select PERSON_PK, SURNAME from KAT_PERSONS WHERE PERSON_PK NOT IN (SELECT OFFICER_PK FROM KAT_OFFICERS) ORDER BY SURNAME";
        try (Statement stmt = HlavniJFrame.dtb.getConnection().createStatement())
            { 
                try (ResultSet rs = stmt.executeQuery(sql))
                {
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
 * Funkce pro zjištění PK úředníka
 */
public void zjistiPKUrednika(){
        
     String sql = "select * from KAT_OFFICERS";

        try (Statement stmt = HlavniJFrame.dtb.getConnection().createStatement())
            { 
                try (ResultSet rs = stmt.executeQuery(sql))
                {
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
 * Při uložení úředníka nastavuje, na kterém indexu se úředník nachází
 */
public void poziceNovehoUrednika(){
    String sql = "select person_pk, NAME, SURNAME, login from KAT_OFFICERS, KAT_PERSONS where KAT_OFFICERS.OFFICER_PK = KAT_PERSONS.PERSON_PK";

        try (Statement stmt = HlavniJFrame.dtb.getConnection().createStatement())
            { 
                try (ResultSet rs = stmt.executeQuery(sql))
                {
                    while (rs.next()) {
                        if(rs.getInt(1) == personPK){
                    parent.index = rs.getRow() - 1;

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

        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        List_seznamOsob = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        TF_login = new javax.swing.JTextField();
        TF_heslo = new javax.swing.JTextField();
        B_ulozitUrednika = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Seznam osob:");

        List_seznamOsob.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        List_seznamOsob.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                List_seznamOsobValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(List_seznamOsob);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Heslo:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Login:");

        TF_login.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        TF_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_loginActionPerformed(evt);
            }
        });

        TF_heslo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        TF_heslo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_hesloActionPerformed(evt);
            }
        });

        B_ulozitUrednika.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        B_ulozitUrednika.setText("Uložit úředníka");
        B_ulozitUrednika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_ulozitUrednikaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(194, 194, 194)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(227, 227, 227)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TF_heslo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TF_login, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(B_ulozitUrednika))))
                .addContainerGap(179, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(TF_login, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TF_heslo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(62, 62, 62)
                .addComponent(B_ulozitUrednika)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void List_seznamOsobValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_List_seznamOsobValueChanged
        index = List_seznamOsob.getSelectedIndex();
        System.out.println(index);

    }//GEN-LAST:event_List_seznamOsobValueChanged

    private void TF_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_loginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_loginActionPerformed

    private void TF_hesloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_hesloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_hesloActionPerformed

    private void B_ulozitUrednikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_ulozitUrednikaActionPerformed

        if(index != -1){
            if(!TF_login.getText().isEmpty() && !TF_heslo.getText().isEmpty()){
                if(!editace){
                    pridejUrednikaDoDB();
                }
                else{
                    upravUrednikaVDB();
                }

                parent.naplnJListUredniku();
                poziceNovehoUrednika();
                parent.List_seznamUredniku.setSelectedIndex(parent.index);
                parent.zobrazUrednika();
                this.dispose();
            }else{
                System.out.println("Musíte vyplnit login a heslo");
            }
        }
        else{
            System.out.println("Nejprve vyberte osobu");
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_B_ulozitUrednikaActionPerformed

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
            java.util.logging.Logger.getLogger(Vloz_urednika1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Vloz_urednika1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Vloz_urednika1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Vloz_urednika1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Vloz_urednika1 dialog = new Vloz_urednika1(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton B_ulozitUrednika;
    private javax.swing.JList<String> List_seznamOsob;
    private javax.swing.JTextField TF_heslo;
    private javax.swing.JTextField TF_login;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
