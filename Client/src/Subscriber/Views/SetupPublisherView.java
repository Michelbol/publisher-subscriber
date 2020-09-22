package Subscriber.Views;

import javax.swing.*;

public class SetupPublisherView {

    private String askInterest(){
        return JOptionPane.showInputDialog(null,"Informe sobre qual interesse será publicado: ");
    }

    private String askPublish(){
        return JOptionPane.showInputDialog(null,"Escreva a publicação: ");
    }

    private void startView(){
        JOptionPane.showMessageDialog(null,"Bem vindo Publicador!");
    }

    public String[] exec(){
        this.startView();
        String interest = this.askInterest();
        while(interest.equals("")){
            this.required("o interesse");
            interest = this.askInterest();
        }
        interest = interest.replace("|", "");

        String message = this.askPublish();
        while(message.equals("") ){
            this.required("a publicação");
            message = this.askPublish();
        }
        message = message.replace("|", "");

        return new String[]{interest, message};
    }

    private void required(String field){
        JOptionPane.showMessageDialog(null, "É obrigatório preencher " + field);
    }
}
