# Milkshake-Login-Android
Sample login pages for Android and integration with Parse Platform backend
About
--
This is a login workflow backed by the Parse platform backend.  This login includes email login and sign up, Facebook login sign up and forgot password.  It comes with a welcome page, login page and sign up page.

Prerequistes
--
This project requires creating a Parse app and a Facebook app.  Follow the instructions here on setting up a Parse account the instuctions here for setting up a Facebook app with your Parse app.

User Experience
--
We believe this login workflow is optimal for mobile from a user experience perspective.  The most controversial design choice is that we do not hide the password text by default.  Due to the small keyboard and personal nature of the device, the typos and failed login attempts outweight any security benefits.  In the event that security is required for entering password, the user may elect to hide the characters like the default behavior.  Failed login attempts cause a greater bounce rate and we want to avoid that at all costs.  

License
--
This project is licensed with the MIT License
