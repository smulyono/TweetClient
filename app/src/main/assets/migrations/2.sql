ALTER TABLE tweet_user ADD COLUMN tagline TEXT;
ALTER TABLE tweet_user ADD COLUMN followers_count INTEGER;
ALTER TABLE tweet_user ADD COLUMN following_count INTEGER;
ALTER TABLE tweet_user ADD COLUMN profile_background_image_url TEXT;
ALTER TABLE tweet_user ADD COLUMN profile_background_color TEXT;

ALTER TABLE tweetclient_user ADD COLUMN tagline TEXT;
ALTER TABLE tweetclient_user ADD COLUMN followers_count INTEGER;
ALTER TABLE tweetclient_user ADD COLUMN following_count INTEGER;
ALTER TABLE tweetclient_user ADD COLUMN profile_background_image_url TEXT;
ALTER TABLE tweetclient_user ADD COLUMN profile_background_color TEXT;