package gov.ca.cwds.jenkins

import spock.lang.Specification

class SlackNotifierSpecification extends Specification {

  class Environment {
    public static final String JOB_NAME = 'myjob'
    public static final String JOB_URL = 'http://job.url'
    public static final String BUILD_DISPLAY_NAME = 'buildDisplayName'
    public static final String BUILD_URL = 'http://job.url'
    public static final String BUILD_ID = 33
  }

  class PipeLineScript {
    def env = new Environment()

    def sh(hash) { }
  }

  def "#notifySlack"() {
    given:
    def payload = '''{
      "attachments": [{
          "color": "#ff0000",
          "fields": [
            {
              "title": "Project",
              "value": "myproject",
              "short": true
            },
            {
              "title": "Job",
              "value": "<http://job.url|myjob>",
              "short": true
            },
            {
                "title": "Build",
                "value": "<http://job.url|buildDisplayName>",
                "short": true
            },
            {
                "title": "Status",
                "value": ":angry_unicorn: FAILED :angry_unicorn:",
                "short": true
            },
            {
                "title": "Error",
                "value": "This is an exception",
                "short": false
            },
            {
                "title": "Stack Trace",
                "value": "[class.method(file:15)]",
                "short": false
            }
        ]
      }]
    }'''

    def error = new Exception('This is an exception')
    def trace = [ new StackTraceElement('class', 'method',  'file', 15) ] as StackTraceElement[]
    error.stackTrace = trace

    PipeLineScript pipeline = Spy(PipeLineScript)

    def curlCommand = "curl -X POST -H 'Content-type: application/json' --data '${payload}' https://exampleslack.url"

    def slackNotifier = new SlackNotifier(pipeline, 'https://exampleslack.url')

    when:
    slackNotifier.notifySlack('myproject', error)

    then:
    1 * pipeline.sh([script: curlCommand])
  }
}
