package gov.ca.cwds.jenkins.licensing

enum LicensingSupportType {
  NONE('None'),
  JK1_GRADLE_LICENSE_REPORT('JK1 Gradle License Report Plugin'),
  RUBY_LICENSE_FINDER('Ruby License Finder Plugin')

  def title

  LicensingSupportType(title) {
    this.title = title
  }
}
