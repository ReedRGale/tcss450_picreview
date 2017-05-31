Group 1: Demyan Pavlovskiy, Jared Lowery, Arrunn Chhouy

Bug fixes:
	Search function did not work - Users are now able to search for reviews by tags. 

	Review functionality did not work - You could not store reviews. Users are now able to create and store reviews.

	No camera feature- Previously we did not implement the camera functionality. It is now working and the app will now prompt
		users to take a picture when they begin the review process.

	Google Maps did not work - Map did not load before because we did not make sure that Google Maps was properly implemented. In this phase, we replaced
		the Google Maps API with the Google Places API. The App can now open a map and allows the user to select a location for the review.

	Login/Register did not take user to main menu after successfully logging in or register and also did not take user anywhere 
		after creating review - We fixed this issue and now after logging in or successfully creating a review 
		the user will be automatically taken to the home page.

	Friendlier U.I. - Phase I U.I. was a little clunky. Navigating the app could prove to be a pain since if you were in the
		middle of reviewing and decided to stop, there was no way to return to the main menu unless you clicked the back
		button several times. To address this, we added a home button to the review process so that the user could return to the
		main menu at any time. We also added short notes to some of the steps to help make sure the user understands what they need to do.
 
Use cases:
	Implemented:
		Register - Users are able to register new accounts to be able to create new reviews. They are immediately logged in as well and taken to
			the main menu.

		Logging into Application - There is a login page for users to login. Once logged in, it will take them to the main menu. They
			will remain logged in until they decide to logout.

		Search for product - Users are able to search for reviews that other users have completed. They
			can search for a review by tag or username.

		Make a picReview - Users are able to create a review. In the process of doing so they will add an Image(required), a caption(optional),
			a tag(optional), and a location(optional). Their review is saved in the database.
		
	Omitted:
		Search for Establishment - We were not able to complete this feature.

Data Storage:
	Login information - We used SQLite to store the login information. We did this to make it more convenient for the user to use our app without
		needing to remember their username and password. We figured that in this day and age, a successful app must be able to be accessed
		without much thought; who needs users with brains anyway? They wouldn't want to watch the constant stream of ads we'll be throwing at
		them soon.

Web Service:
	PHP: Login, Register, MakeReview, GetUserReviews, GetAllReviews, UpdateReview
		There are currently 6 different PHP files that we hit in order to store and retrieve data. They are used for logging in/registering users
		as well as making and retrieving reviews. The names of the files speak for themselves.
	
	Google Places API: We hit this API if the reviewer gives a location for their review. It opens up a map, detects the user's location, allows the user
		to change the location, and assigns that location to the review. This location is displayed on the review so that interested users can 
 		visit the location.

Graphics: While we do have an icon for the app, we do not have a logo.

Meeting Notes:
https://docs.google.com/document/d/1G8prk6YGUdjxs_VcpVb5b8923EaEMYFbiTQG9Anf7uc/edit?ts=58e8a0a6