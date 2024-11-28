
# RMIT COSC2299 SEPT Major Project

# Group Information

## Group-P5-02

## Members
* Mashal Ahmad Khan (s3906303)
* Darian Gin (s3967454)
* Matthew Dalangin (s3944263)
* Ravikaran Singh (s3940606)
* Owen Cashin (s4008352)
* Sandro Mortimer (s4003020)

## Records

* Github repository: https://github.com/cosc2299-2024/team-project-group-p05-02.git
* Github Project Board: https://github.com/orgs/cosc2299-2024/projects/12
* Microsoft Teams Group: https://teams.microsoft.com/l/team/19%3AsC-3CPbbJcAnTT0Gj1ItbPVkDeosX47zixl3tuRkRSs1%40thread.tacv2/conversations?groupId=879815be-3b3a-47ca-9cec-76faf6cc41a2&tenantId=d1323671-cdbe-4417-b4d4-bdb24b51316b

See [Instructions](INSTRUCTIONS.md)

## VScode springboot quick run
To allow running and restarting of the application with a single keypress:

With the [Spring Boot Extension Pack](https://marketplace.visualstudio.com/items?itemName=vmware.vscode-boot-dev-pack) installed,
Press `ctrl+shift+p`, then go to `Preferences: Open Keyboard Shortcuts (JSON)`, then add the following
```JSON
[
    {
        "key": "f5",
        "command": "spring-boot-dashboard.localapp.run-multiple",
        "when": "view == 'spring.apps'"
    }
]
```

## Refactoring to-do list
- [ ] Move all html pages into the main templates folder unless there is multiple html pages for a single feature
- [ ] Combine all instances of `Connection connection = this.source.getConnection(); PreparedStatement stm = connection.prepareStatement(query)`into a single generic database communication method.
- [ ] Create generic functionality for handling SQLException.
- [ ] Remove unused exceptions (no yellow or red files).
- [ ] Either add or remove Impl/Structure classes, just keep it consistent.
- [ ] (Where applicable) remove and fix all TODO's
- [ ] Delete or rename all generic/default files