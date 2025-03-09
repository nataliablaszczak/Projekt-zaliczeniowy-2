Feature: Test checkout process on MyStore

  Scenario: User is logged in and completes checkout process

    Given The user is logged in with username "login" and password "password"
    When The user adds "Hummingbird Printed Sweater" to the cart with size "M" and quantity "5"
    And The user proceeds to the checkout page
    And The user confirms the address
    And The user selects "Pick up in store" as the delivery method
    And The user chooses "Pay by Check" as the payment method
    Then The user should see an order confirmation with the total amount
    And The user will take a screenshot of the order confirmation