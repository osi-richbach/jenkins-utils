package gov.ca.cwds.jenkins

import gov.ca.cwds.jenkins.common.BuildMetadata
import gov.ca.cwds.jenkins.licensing.LicensingSupport

def call(branchName, sshCredentialsId, options = [:]) {
  def buildMetadata = new BuildMetadata(this, this.env.JOB_NAME, this.env.BUILD_ID, this.env.WORKSPACE)
  def licensingSupport = new LicensingSupport(this, options.runtimeGradle, options.dockerImage)
  licensingSupport.updateLicenseReport(branchName, sshCredentialsId, buildMetadata)
}
