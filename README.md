FormerBot is a twitch bot made in Java by FormerCanuck.

Features:
	- Ability to add custom commands.
	- Youtube command > Will pull the last upload to the streams youtube channel(currentlty only looks for their twitch name on youtube). Will display the number of days since last upload the title and the link.
	- Points system
		- Currently followers gain 1 point/minute, it is configurable but only in the config as of now.
		- Points can be given/taken via command !points <add | remove> <amount> <user>
		- Dueling system to allow users to challange other users to duels for points.
	- The bot will auto clear chat defaulted to 30 minutes, and is changeable by using !autoclear <minutes> (if argument is empty it will toggle the autoclear function.)
	- Followers are able to type !followage and see when they followed and how long it has been since. !followage top 