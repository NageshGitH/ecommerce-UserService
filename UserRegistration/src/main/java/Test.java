import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test 
{
	public static void main(String args)
	{
		String password=  new BCryptPasswordEncoder().encode("Nagesh@123");
		
		System.out.println("password:::"+password);
	}
}
