import gov.ca.cwds.jenkins.BuildDiscarderDefaults

def call(String buildType = null) {
  def buildDiscarderDefaults = new BuildDiscarderDefaults()
  buildDiscarderDefaults.configure(buildType)
}
