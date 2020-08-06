package gov.ca.cwds.jenkins.semver

import spock.lang.Specification
import spock.lang.Unroll

class NewTagGeneratorSpecification extends Specification {

  @SuppressWarnings('UnnecessaryBooleanExpression')
  @Unroll('For #tags with increment #increment is #expected')
  def "#newTag"() {
    setup:
    def newTagGenerator = new NewTagGenerator()

    expect:
    newTagGenerator.newTag(tags, increment) == expected

    where:
    tags | increment || expected
    [] | IncrementTypes.PATCH || '0.1.1'
    ['0.1.0', '2.1.0'] | IncrementTypes.MAJOR || '3.0.0'
    ['0.1.0', '2.1.0'] | IncrementTypes.MINOR || '2.2.0'
    ['0.1.0', '2.1.0'] | IncrementTypes.PATCH || '2.1.1'
    ['100.0.1', '99.1.0', '1.1.999'] | IncrementTypes.MAJOR || '101.0.0'
    ['100.0.1', '99.1.0', '1.1.999'] | IncrementTypes.MINOR || '100.1.0'
    ['100.0.1', '99.1.0', '1.1.999'] | IncrementTypes.PATCH || '100.0.2'
  }
}
