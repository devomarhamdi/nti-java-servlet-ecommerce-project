package util;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private static final int COST = 10;

    private PasswordUtil(){}

    public static String hash(String plainPassword){
            return BCrypt.hashpw(plainPassword,BCrypt.gensalt(COST));
    }

    public static boolean matches(String plainPassword, String storedHash){
        if (storedHash == null || storedHash.isEmpty()){
            return false;
        }
        try{
            return BCrypt.checkpw(plainPassword, storedHash);
        } catch (IllegalArgumentException e){
            return false;
        }
    }


}
