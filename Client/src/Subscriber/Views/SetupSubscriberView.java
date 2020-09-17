package Subscriber.Views;

import javax.swing.*;

public class SetupSubscriberView {

    private String askName(){
        return JOptionPane.showInputDialog(null,"Informe seu Nome: ");
    }

    private String askInterest(){
        return JOptionPane.showInputDialog(null,"Informe seu Interesse: ");
    }

    private void startView(){
        JOptionPane.showMessageDialog(null,"Bem vindo Assinante!");
    }

    public String[] exec(){
        this.startView();
        String name = this.askName();
        if(name.equals("")){
            while(!this.confirm()){
                this.askName();
            }
        }
        String interest = this.askInterest();
        while(interest.equals("") ){
            this.required();
            this.askInterest();
        }
        return new String[]{name, interest};
    }

    private boolean confirm(){
        return JOptionPane.showConfirmDialog(null, "Tem certeja que não irá preencher "+ "Nome") == 1;
    }

    private void required(){
        JOptionPane.showMessageDialog(null, "É obrigatório preencher o interesse");
    }
}
