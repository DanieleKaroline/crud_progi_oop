package classes;

import java.util.ArrayList;
import java.util.List;

public class ChecksData
{
    private String cpf;
    private String email;
    private String password;
    private String name;
    private String address;

    public ChecksData(String cpf, String email, String password, String name, String address) throws Exception {
        setCpf(cpf);
        setEmail(email);
        setPassword(password);
        setName(name);
        setAddress(address);
    }
    
    public ChecksData(String senha) throws Exception
    {
        setPassword(senha);
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    
    public void setAddress(String address) throws Exception {
        if (address.length() > 0) this.address = address;
        else throw new Exception("This address isn't valid!");
    }
    
    public void setName(String name) throws Exception {
        if (name.length() > 0) this.name = name;
        else throw new Exception("This name isn't valid!");
    }
    
    public void setCpf(String cpf) throws Exception {
        if (!cpfLength(cpf)) throw new Exception("This CPF isn't valid!");
        if (checksFirstDigit(cpfToArray(cpf)) && checksSecondDigit(cpfToArray(cpf))) this.cpf = cpf;
        else throw new Exception("This CPF isn't valid!");
    }

    public void setEmail(String email) throws Exception {
        char[] array = new char[email.length()];
        
        for (int i = 0; i < email.length(); i++) {
            array[i] =  email.charAt(i);
            if (array[i] == '@')
            {
                this.email = email;
                return;
            }
        }
        throw new Exception("This isn't a valid email!");
    }

    public void setPassword(String password) throws Exception {
        if (password.length() == 6) this.password = password;
        else throw new Exception("Passwords must have 6 digits!");
    }
    
    
    private boolean cpfLength(String cpf)
    {
        return cpf.length() == 11;
    }
    
    private List<Integer> cpfToArray(String cpf)
    {
        try
        {
            List<Character> arrayC = new ArrayList<>();
            List<Integer> arrayI = new ArrayList<>();
            
            for (int i = 0; i < 11; i++)
                arrayC.add(cpf.charAt(i));
            
            arrayC.forEach(e ->
                arrayI.add(Character.getNumericValue(e)));
            
            return arrayI;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    private boolean checksFirstDigit(List<Integer> cpf)
    {
        int j = 2, somaMulti = 0, digito = 0;
        for (int i = 8; i > -1; i--)
        {
            somaMulti += cpf.get(i)*j;
            j++;
        }
        if (somaMulti%11 >= 2) digito = 11 - (somaMulti%11);
        return digito == cpf.get(9);
    }
    
    private boolean checksSecondDigit(List<Integer> cpf)
    {
        int j = 2, somaMulti = 0, digito = 0;
        for (int i = 9; i > -1; i--)
        {
            somaMulti += cpf.get(i)*j;
            j++;
        }
        if (somaMulti%11 >= 2) digito = 11 - (somaMulti%11);
        return digito == cpf.get(10);
        
    }
}
