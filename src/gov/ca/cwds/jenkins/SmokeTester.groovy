package gov.ca.cwds.jenkins

class SmokeTester {
  def script

  SmokeTester(script) {
    this.script = script
  }

  def runSmokeTest(path, url) {
    def test = script.sh (script: "${path} ${url}", returnStdout: true).trim()
    if (test.contains('smoketest passed')) {
      script.echo 'smoke test passed'
      return 'smoke test has passed'
    }
    script.error ("'${test}'")
  }

}
