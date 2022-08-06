package classes;

import javax.swing.JOptionPane;

public class ChecksPaymentData
{
    private String cardNumber;
    private String expirationDate;
    private String controlDigits;

    public ChecksPaymentData(String cardNumber, String expirationDate, String controlDigits) throws Exception {
        setCardNumber(cardNumber);
        setExpirationDate(expirationDate);
        setControlDigits(controlDigits);
    }
    
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) throws Exception
    {
            checksIfEmpty(cardNumber);
            checksIfNumber(cardNumber);
            this.cardNumber = cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) throws Exception {
            checksIfEmpty(expirationDate);
            checksIfNumber(expirationDate);
            this.expirationDate = expirationDate;
    }

    public String getControlDigits() {
        return controlDigits;
    }

    public void setControlDigits(String controlDigits) throws Exception {
            checksIfEmpty(controlDigits);
            checksIfNumber(controlDigits);
            this.controlDigits = controlDigits;
    }
    
    public void checksIfNumber(String data) throws Exception
    {
        try
        {
            int dataInt = Integer.parseInt(data);
        } catch (NumberFormatException e) { 
            throw new Exception("Payment data isn't valid!"); }
    }
    
    public void checksIfEmpty(String data) throws Exception
    {
        if (data.length() == 0) throw new Exception("Payment data isn't valid!");
    }
    
}
