import org.apache.commons.cli.ParseException;

import s3exception.InvalidHeaderException;
import s3exception.InvalidOpException;
import s3exception.InvalidOptionException;
import s3exception.InvalidParamException;

public class S3Cmd {
    public static ParseArgs parse_arg = new ParseArgs();
    public static S3Op s3_op = new S3Op(parse_arg);
    
    public static void main(String[] args) {
        try {
            parse_arg.init();
            parse_arg.parse(args);
	        s3_op.init();
	        s3_op.process_request();
        } catch (InvalidOpException e) {
            System.out.println("Error: invalid op type.");
        } catch (ParseException e) {
            System.out.println("Error: parameters parse fail.");
        } catch (InvalidHeaderException e) {
            System.out.println("Error: invalid http header.");
        } catch (InvalidOptionException e) {
            System.out.println("Error: invalid option.");
        } catch (InvalidParamException e) {
            System.out.println("Error: invalid http parameter.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    
}
