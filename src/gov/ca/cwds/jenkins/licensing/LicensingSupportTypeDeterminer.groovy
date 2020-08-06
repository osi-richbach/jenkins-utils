package gov.ca.cwds.jenkins.licensing

import gov.ca.cwds.jenkins.common.ProjectTypes

class LicensingSupportTypeDeterminer {
  def pipeline

  LicensingSupportTypeDeterminer(pipeline) {
    this.pipeline = pipeline
  }

  LicensingSupportType determineLicensingSupportType(buildMetadata) {
    def licensingSupportType = LicensingSupportType.NONE
    def projectTypes = buildMetadata.projectTypes()
    if (projectTypes.contains(ProjectTypes.JAVA) && projectHasJK1GradleLicenseReportPlugin()) {
      licensingSupportType = LicensingSupportType.JK1_GRADLE_LICENSE_REPORT
    } else if (projectTypes.contains(ProjectTypes.RUBY) && projectHasRubyLicenseFinderPlugin()) {
      licensingSupportType = LicensingSupportType.RUBY_LICENSE_FINDER
    }
    pipeline.echo "Detected Licensing Support Type: ${licensingSupportType.title}"
    licensingSupportType
  }

  private projectHasJK1GradleLicenseReportPlugin() {
    pipeline.sh(script: 'grep -c "com.github.jk1.dependency-license-report" build.gradle',
      returnStatus: true) == 0
  }

  private projectHasRubyLicenseFinderPlugin() {
    pipeline.sh(script: 'grep -c "license_finder" package.json', returnStatus: true) == 0
  }
}
