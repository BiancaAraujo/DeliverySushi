
public class Verificacao {

	public static boolean isDate(String s) {
		if(s.length() != 10)
			return false;
		if(s.charAt(4)!='/'||s.charAt(7)!='/')
			return false;
		if(s.charAt(0)<'0'||s.charAt(0)>'9'||s.charAt(1)<'0'||s.charAt(1)>'9'||s.charAt(2)<'0'||s.charAt(2)>'9')
			return false;
		if(s.charAt(3)<'0'||s.charAt(3)>'9'||s.charAt(5)<'0'||s.charAt(5)>'1'||s.charAt(6)<'0'||s.charAt(6)>'9')
			return false;
		if(s.charAt(8)<'0'||s.charAt(8)>'3'||s.charAt(9)<'0'||s.charAt(9)>'9')
			return false;
		return true;
		
	}
	public static boolean isFloat(String s) {
	    try { 
	        Float.parseFloat(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    if(Float.parseFloat(s) < 0)
	    	return false;
	    return true;
	}
	
	  public static boolean isInteger(String s) {
		    try { 
		        Integer.parseInt(s); 
		    } catch(NumberFormatException e) { 
		        return false; 
		    } catch(NullPointerException e) {
		        return false;
		    }
		    return true;
		}
}
