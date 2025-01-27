package MainFinal;

import java.util.ArrayList;
import java.util.Scanner;

public class MainFinal {

    static Scanner sc = new Scanner(System.in);
    // Accunt : Name , number AC , Money , PIN
    static String[][] account = {
            { "Jaxon Storm", "987-654-3210", "102421", "582493" },
            { "Raven Steele", "321-098-7654", "91010", "761820" }
    };
    // เก็บเป็นบัญชีต้นทางหลังเข้าสู่ระบบมันต้องGolbalเพื่อให้เอาตำแหน่งไปใช้ได้ในทุกอัน
    static int loggedInAccountIndex = -1;
    // ArrayList สำหรับเก็บรายการเคลื่อนไหวของบัญชี
    static ArrayList<String> accountStatement = new ArrayList<>();

    public static void main(String[] args) {
        showME();
        while (true) {
            System.out.println("\n[---- (1). Login OR  (2). Close Program ----]");
            System.out.print("Enter (number) : ");
            int enter = 0;
            try {
                enter = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\n Please enter a numeric value. !!!");
            }
            if (enter == 2) {
                showME();
                break;
            } else if (enter == 1) {
                boolean userNeed = login();
                while (userNeed) { // Pass
                    switch (selectMenu()) {
                        case 1 -> transferFunds();
                        case 2 -> deposit();
                        case 3 -> withdraw();
                        case 4 -> System.out.println(balance());
                        case 5 -> printAccountStatement();
                        case 6 -> load();
                        case 7 -> changePIN();
                        case 8 -> {
                            closeAccount();
                            userNeed = false;
                        }
                        case 9 -> {
                            userNeed = false;
                            logout();
                        }
                        default -> System.out.println();
                    }
                    System.out.println();
                }
            }
        }
    }

    public static void showME() {
        for (String[] Acount1 : account) {
            System.out.println("--------------------------\nPerson :" + Acount1[0] +
                    "\nNumber Account : " + Acount1[1] +
                    "\nMoney : " + Acount1[2] +
                    "\n\n# hide User PIN --> " + Acount1[3] +
                    "\n--------------------------");
        }
    }

    public static int selectMenu() {
        System.out.println("""
                --- Main Menu ---
                1. Transfer Funds
                2. Deposit Funds
                3. Withdraw Money
                4. View Balanc
                5. Account Statement
                6. Loan
                7. Change PIN
                8. Close Account
                9. Logout """);
        System.out.print("Choose an option (Type Number) : ");
        int option = 0;
        try {
            option = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("\n Please enter a numeric value. !!!\n");
        }
        System.out.println("-----> \n");
        return option;
    }

    // Ping
    public static boolean login() {
        System.out.println("======================================");
        System.out.println("Please log in");
        System.out.print("Please enter your account number : ");
        String accountNumber = sc.nextLine();
        System.out.print("Enter pin : ");
        String pin = sc.nextLine();

        for (int i = 0; i < account.length; i++) {
            if (account[i][1].equals(accountNumber) && account[i][3].equals(pin)) {
                loggedInAccountIndex = i; // เก็บตำแหน่งของบัญชีที่เข้าสู่ระบบ
                System.out.println("Login successful.");
                System.out.println("======================================");
                return true;
            }
        }
        System.out.println("User account not found or the PIN code is wrong. Please try again.");
        return false;
    }

    // Ping
    public static void transferFunds() {

        System.out.print("Please enter the destination account number : ");
        String toAccount = sc.nextLine();

        // ค้นหาบัญชีปลายทางในระบบ
        int toIndex = findAccountIndex(toAccount);

        // ตรวจสอบว่าพบทบัญชีปลายทางในระบบ
        if (toIndex == -1) {
            System.out.println("Account Not found.");
            return;
        }
        double amount; // กำหนดก่อนเพื่อที่จะได้ไม่ Error เพราะตัวแปรอื่นก็ใช้ แล้วถ้าไม่มีการ declear
        // ของ amount ไว้ก่อนจะมีปัญหาเพราะมันหาไม่เจอ
        try {
            System.out.print("Please enter the amount you wish to transfer: ");
            amount = Double.parseDouble(sc.nextLine()); // ติด Buffer คือเรามันมีการติดตัวของ "เว้นวรรค" ที่เป็น
            // String (เมื่อเราใช้งาน .nextLine)
        } catch (NumberFormatException e) {
            System.out.println("\n Invalid amount entered. Please enter a numeric value. !!!   ");
            return; // หยุดฟังก์ชันหากข้อมูลบัญชีต้นทางผิดพลาด
        }
        // ตรวจสอบยอดเงินของบัญชีต้นทาง
        double fromBalance = Double.parseDouble(account[loggedInAccountIndex][2]);
        if (fromBalance < amount) {
            System.out.println("The account balance is insufficient.");
            return;
        }

        // ดำเนินการโอนเงิน
        account[loggedInAccountIndex][2] = String.valueOf(fromBalance - amount);
        account[toIndex][2] = String.valueOf(Double.parseDouble(account[toIndex][2]) + amount);

        System.out.println(
                "Transfered successfully amount " + amount + " Bath from " + account[loggedInAccountIndex][0]
                        + " to "
                        + account[toIndex][0]);

        // Google
        accountStatement.add("Transfer: " + amount + " Bath to " + account[toIndex][0] +
                " \n|\t !! New Balance: " + account[loggedInAccountIndex][2] + " Bath ");
    }

    // ค้นหาบัญชีด้วยหมายเลขบัญชี
    public static int findAccountIndex(String accountNumber) {
        for (int i = 0; i < account.length; i++) {
            if (account[i][1].equals(accountNumber)) {
                return i;
            }
        }
        return -1;
    }

    // Chimon เพราะเอาไปใช้ใน Merthod เพื่อ Return print ใน Sysout
    public static String balance() {
        return "The Account " + "(" + account[loggedInAccountIndex][0] + ") " + account[loggedInAccountIndex][1]
                + " remaining balance is "
                + account[loggedInAccountIndex][2] + " Bath.";
    }

    // Chimon
    public static void closeAccount() {
        while (true) {
            System.out.print("Enter the account number you wish to close : ");
            String accountID = sc.nextLine();
            System.out.print("Enter PIN : ");
            String accountPIN = sc.nextLine();
            boolean accountFound = false;

            if (accountID.equalsIgnoreCase(account[loggedInAccountIndex][1])
                    && accountPIN.equalsIgnoreCase(account[loggedInAccountIndex][3])) {
                // เก็บตำแหน่งที่ต้องการลบ
                accountFound = true;
                System.out.println("Success: Account " + account[loggedInAccountIndex][1] +
                        " (" + account[loggedInAccountIndex][0] + ") has been closed.");
            }

            if (!accountFound) {
                System.out.println("User account not found or the PIN code is wrong. Please try again.");
            } else {
                /*
                 * คือเราจะสร้าง array ใหม่เพื่อโดนลบ -1 index (1 บช)
                 * แล้วก็มาคัดลอกข้อมูลที่ไม่ลบลงใน Array ใหม่แล้วส่งค่ากลับไปหา Acconut
                 */
                String[][] newArr = new String[account.length - 1][];
                int l = 0;
                for (int k = 0; k < account.length; k++) {
                    if (k != loggedInAccountIndex) {
                        newArr[l++] = account[k]; // คัดลอกข้อมูลที่ไม่ถูกลบ ( l++ คือหลังจากผ่านตรงนี้ไป l = 1)
                    }
                }
                account = newArr.clone();
                break;
            }
        }
    }

    // FAIฝากเงิน
    public static void deposit() {
        System.out.print("Please enter the amount you wish to deposit : ");
        double amount;
        try {
            amount = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("\n Invalid amount entered. Please enter a numeric value. !!! ");
            return; // หยุดฟังก์ชันหากข้อมูลบัญชีต้นทางผิดพลาด
        }
        if (amount <= 0) {
            System.out.println("Please enter an amount greater than 0 !");
        }

        // อัปเดตยอดคงเหลือ
        account[loggedInAccountIndex][2] = String
                .valueOf(Double.parseDouble(account[loggedInAccountIndex][2]) + amount);
        System.out.println("Deposit successful!! \n" + balance());

        // Google
        accountStatement.add(
                "Deposit: " + amount + " Bath \n|\t !! New Balance : " + account[loggedInAccountIndex][2] + " Bath. ");
    }

    // FAIสินเชื่อ
    public static void load() {
        double creditLimit = 50000.0;
        double loanAmount;

        System.out.print("Please enter the amount you wish to borrow : ");
        try {
            loanAmount = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("\n Invalid amount entered. Please enter a numeric value. !!!  ");
            return; // หยุดฟังก์ชันหากข้อมูลบัญชีต้นทางผิดพลาด
        }
        // ตรวจสอบการขอสินเชื่อ
        if (loanAmount <= 0) {
            System.out.println("Please enter an amount greater than 0 !");
            return;
        }
        if (loanAmount > creditLimit) {
            System.out.println("Credit limit exceeded (maximum limit: " + creditLimit + " Bath)");
            return;
        }


        // คำนวนดอกเบี้ย
        double totalAmountToRepay = loanAmount + (loanAmount * 0.05);

        // อัปเดตยอดคงเหลือ
        account[loggedInAccountIndex][2] = String
                .valueOf(Double.parseDouble(account[loggedInAccountIndex][2]) + loanAmount);

        System.out.println(
                "\nLoan Approved!! Amount Borrowed " + loanAmount + " Bath " + "\nInterest: "
                        + (loanAmount * 0.05) + " Bath"
                        + "\nTotal amount to be repaid: " + totalAmountToRepay + " Bath.");
        // Google
        accountStatement.add("Loan: " + loanAmount + " Bath with interest " + (loanAmount * 0.05) +
                " Bath | Total repayment: " + totalAmountToRepay + " Bath \n|\t !! New Balance: "
                + account[loggedInAccountIndex][2] + " Bath");
    }

    // Google
    public static void logout() {
        System.out.println("Logout successful.");
    }

    // Google
    public static void printAccountStatement() {
        System.out.println("\n--- Account Statement ---");
        if (accountStatement.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (String transaction : accountStatement) {
                System.out.println(transaction);
            }
        }
        System.out.println("--------------------------");
    }

    // Copy ถอนเงิน
    public static void withdraw() {
        double amount;
        System.out.print("Enter the amount to withdraw: ");
        try {
            amount = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("\n Invalid amount entered. Please enter a numeric value. !! ");
            return; // หยุดฟังก์ชันหากข้อมูลบัญชีต้นทางผิดพลาด
        }
        double currentBalance = Double.parseDouble(account[loggedInAccountIndex][2]);

        if (amount <= 0) { // เตือนว่า เงินที่ถอนต้องมากกว่า 0
            System.out.println("Please enter an amount greater than 0.");
        } else if (amount > currentBalance) { // เตือนว่า ยอดเงินคงเหลือไม่เพียงพอที่จะถอน
            System.out.println("Insufficient balance.");
        } else { // อัปเดตยอดคงเหลือและบันทึกรายการเคลื่อนไหวของบัญชี
            account[loggedInAccountIndex][2] = String.valueOf(currentBalance - amount);
            System.out.println("Withdraw successful. \n" + balance());
            accountStatement.add("Withdraw: " + amount + " Bath \n|\t !! New Balance: "
                    + account[loggedInAccountIndex][2] + " Bath");
        }
    }

    // Copy เปลี่ยน PIN
    public static void changePIN() {
        System.out.print("Enter your current PIN: ");
        String currentPIN = sc.nextLine();

        // check รหัสปัจจุบันว่า ตรงกับ
        // Account[loggedInAccountIndex][3](รหัสที่บันทึกไว้ใน array) ไหม
        if (!currentPIN.equals(account[loggedInAccountIndex][3])) {
            System.out.println("Incorrect PIN. Try again.");
            return;
        }

        System.out.print("Enter new PIN: ");
        String newPIN = sc.nextLine();
        System.out.print("Confirm new PIN: ");
        String confirmNewPIN = sc.nextLine();

        // check newPIN กับ confirmPIN ตรงกันไหม
        if (!newPIN.equals(confirmNewPIN)) {
            System.out.println("PINs do not match. Please try again.");
            return;
        }

        System.out.print("Enter your current PIN to confirm the change: ");
        String verifyCurrentPIN = sc.nextLine();

        // check verifyCurrentPIN กับ
        // Account[loggedInAccountIndex][3](รหัสที่บันทึกไว้ใน array) ตรงกันไหม
        if (verifyCurrentPIN.equals(account[loggedInAccountIndex][3])) {
            account[loggedInAccountIndex][3] = newPIN;
            System.out.println("PIN changed successfully.");
        } else {
            System.out.println("Incorrect current PIN. PIN change unsuccessful.");
        }
    }

}