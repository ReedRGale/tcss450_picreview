
Bug fixes:
	Search function does not work - Users are now able to search for reviews by tags. 

	Review function does not work - The review function was not working all the way. It was only able to store 
	text. However users are now able to save images and other information such as captions, tags, and etc.. 
	for their review. 

	No camera - Previously we did not implement the camera functionality. It is now working and users can
	take pictures through the app so that they can use it in a review.

	Google Maps not displaying - Google Maps was not working on almost all of the devices. It was working 
	only on one of our group members computer. The way we had address this issue is that we ended up replacing
	the Google Maps API with Google Place API. There is a Google Place Picker that we are using that can find 
	and grab the location that the user selected. This serves the purpose that we can save a review by a location
	that the user is currently located.

	Login does not go to home page or does not take user anywhere new - After logging in the user was still at 
	the same page. We fixed this issue and now after logging in the user will be navigating to the home page where
	they will be able to search, review, or look at their own reviews.

	Smooth Navigation - During phase 1 our app did not have smooth navigation through different pages. We added 
	a couple of button at the bottom of when you are in the process of reviewing. There is a forward, back and home
	button that the users can use to go through each step of the review process. In the case that they need to 
	change some details they can always go back and update it.
 
Use cases:
	Implemented:
		Register - Users are able to register new accounts to be able to create new reviews

		Logging into Application - There is a login page for users to login. It will also keep them 
		login into the appliation until they chose to logout. If they do not logout they will remained logged in
		for convenience when they reopen the application.

		Search for product - Users are able to search for reviews that other users have completed. They
		can search for a review by the tag.

		Make a picReview - Users will be guided through the process of creating a review. There will be options
		that allow them to add a caption, tag, and location. Lastly there will be a page where the user 
		can look over their review before submitting it.
		
	Omitted:
		Search for Establishment - We were not able to complete it

Data Storage:
	Login information - We used SQLite to store the login information. This will make it easier for user so 
	they do not have to login again. Once they opened the app they will automatically be logged in and they 
	will be brought to the home page where they can search or review. 

Web Service:
	PHP: Login, Register, Make Review, Get User Review, Get All Review, Update Review
	There are currently 6 different web services through PHP that we are using.  These services will allow us to  
	get all the functions that we need to make this review application work properly. Since this application
	is about reviewing, we want to be able to save the opinion of the user into a database that is why we have some web service.
	There is the Login/Register service so that users can create an account. Make review allows the user to save
	the review. Get User/ Get All Review gives the ability to search for a review in the system. 
	Lastly users can update their review through the Update Review service.
	
	Google Places API: We are using the Google Places API because it has all the information on places around the 
	world. This information will allow us to save review by location and users will be able to look up for review 	
	and see where the review is located.


Meeting Notes:
https://docs.google.com/document/d/1G8prk6YGUdjxs_VcpVb5b8923EaEMYFbiTQG9Anf7uc/edit?ts=58e8a0a6