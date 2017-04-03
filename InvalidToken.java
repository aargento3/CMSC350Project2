/**
 *
 * @author AArgento
 * @date 9 April 2017
 * @class CMSC 350
 * @purpose Define custom InvalidToken exception
 *
 */

public class InvalidToken extends Exception {

    public InvalidToken() {
        System.out.println("Expression contained Invalid Token.");
    }
}//end InvalidToken