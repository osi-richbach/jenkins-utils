package gov.ca.cwds.jenkins

class SlackNotifier {
  def script
  def webhookUrl

  SlackNotifier(script, webhookUrl) {
    this.script = script
    this.webhookUrl = webhookUrl
  }

  def notifySlack(projectName, error) {
    def payload = """{
      "attachments": [{
          "color": "#ff0000",
          "fields": [
            {
              "title": "Project",
              "value": "${projectName}",
              "short": true
            },
            {
              "title": "Job",
              "value": "<${this.script.env.JOB_URL}|${this.script.env.JOB_NAME}>",
              "short": true
            },
            {
                "title": "Build",
                "value": "<${this.script.env.BUILD_URL}|${this.script.env.BUILD_DISPLAY_NAME}>",
                "short": true
            },
            {
                "title": "Status",
                "value": ":angry_unicorn: FAILED :angry_unicorn:",
                "short": true
            },
            {
                "title": "Error",
                "value": "${error.message}",
                "short": false
            },
            {
                "title": "Stack Trace",
                "value": "${error.stackTrace}",
                "short": false
            }
        ]
      }]
    }"""

    script.sh(script: "curl -X POST -H 'Content-type: application/json' --data '${payload}' ${webhookUrl}")
  }
}
