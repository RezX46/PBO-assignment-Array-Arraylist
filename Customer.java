import java.util.ArrayList;

public class Customer {
    private String firstName;
    private String lastName;
    private ArrayList<Account> accounts;

    public Customer(String f, String l) {
        this.firstName = f;
        this.lastName = l;
        this.accounts = new ArrayList<Account>();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setAccount(Account acct) {
        accounts.add(acct);
    }

    public Account getAccount(int account_index) {
        if (account_index >= 0 && account_index < accounts.size()) {
            return accounts.get(account_index);
        }
        return null;
    }

    // Overload getAccount() untuk mendapatkan akun pertama
    public Account getAccount() {
        return accounts.isEmpty() ? null : accounts.get(0);
    }

    public int getNumOfAccounts() {
        return accounts.size();
    }
}
