import java.math.BigInteger;

public class BigNumTest{
    public static void main(String[] args){
        System.out.println("Hello,BigNum");

        String bookId = "5d412a898a245f6cbbc2e356";
        String newBookId = convertBookId(bookId);
        System.out.println(newBookId);
    }

    public static String convertBookId(String BookId){
        BigInteger id = new BigInteger(BookId,16);
        id = id.add(BigInteger.valueOf(2));
        return id.toString(16);

    }
}