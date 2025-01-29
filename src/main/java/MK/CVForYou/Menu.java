package MK.CVForYou;


import org.apache.commons.cli.ParseException;
public interface Menu
{
    public Application parse(String args[]) throws ParseException;
}
