/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author strmiska
 */
public class Nemovitosti extends javax.swing.JPanel {

    /**
     * Creates new form Nemovitosti
     */
    public Nemovitosti() {
        initComponents();
    }
    public void inicializuj() throws SQLException, IOException{
        pozemky.inicializuj();
        stavby.inicializuj();
    }
    public void zobrazOkno(int index){
       jTabbedPane1.setSelectedIndex(index);
    }
    public Pozemky getPozemky(){
         return pozemky;
    }
    public Stavby getStavby(){
         return stavby;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        pozemky = new gui.Pozemky();
        stavby = new gui.Stavby();

        jTabbedPane1.addTab("Pozemky", pozemky);
        jTabbedPane1.addTab("stavby", stavby);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 708, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 538, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    private gui.Pozemky pozemky;
    private gui.Stavby stavby;
    // End of variables declaration//GEN-END:variables
}