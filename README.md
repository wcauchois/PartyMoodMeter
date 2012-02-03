Getting started with android_app
--------------------------------

Open Eclipse, go to Window -> Preferences -> Java -> Code Style -> Formatter -> Import...

Then select eclipse_format.xml from the project dir.


Getting started with web_app
----------------------------

    $ virtualenv venv --distribute
    $ source venv/bin/activate
    $ pip install -r requirements.txt
    $ git remote add heroku git@heroku.com:partymoodmeter.git

To deploy to Heroku:

    $ git push heroku master

To run a local devserver:

    $ ./web_app/manage.py runserver
