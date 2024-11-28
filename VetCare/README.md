NOTE: The code here only reflects the parts I completed, not the finished product. My contributions were the appointment booking and view booking functionality, I am currently working on bringing the final codebase to completion before exhibiting it in this portfolio.
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
