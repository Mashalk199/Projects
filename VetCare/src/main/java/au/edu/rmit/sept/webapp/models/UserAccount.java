package au.edu.rmit.sept.webapp.models;

public record UserAccount(int userId, String username, String password, String role, String firstName, String lastName,
        String email, String address, Integer vetId) {


    

    // Override of equals() for UserAccounts
    @Override
    public boolean equals(Object o) {
        if (this.getClass() == o.getClass()) {
            UserAccount account = (UserAccount) o;
            if (this.userId == account.userId &&
                    this.username.strip().equals(account.username.strip()) &&
                    this.password.strip().equals(account.password.strip()) &&
                    this.role.strip().equals(account.role.strip()) &&
                    this.firstName.strip().equals(account.firstName.strip()) &&
                    this.lastName.strip().equals(account.lastName.strip()) &&
                    this.email.strip().equals(account.email.strip()) &&
                    this.address.strip().equals(account.address.strip()) &&
                    this.vetId == account.vetId) {
                return true;
            }
        }
        return false;

    }


}
