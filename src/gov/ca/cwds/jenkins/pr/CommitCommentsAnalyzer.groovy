package gov.ca.cwds.jenkins.pr

class CommitCommentsAnalyzer {
  /**
   *
   * @param commitComments list of commit comments
   * @return map where keys are CommitTypeGroups and values are lists of comments related to each CommitTypeGroup
   */
  static groupCommitComments(List<String> commitComments) {
    Map<CommitTypeGroups, List<String>> groupedComments = [:]
    commitComments.each { comment ->
      CommitTypeGroups.values().each { commitTypeGroup ->
        if (isCommentInGroup(comment, commitTypeGroup)) {
          if (!groupedComments.containsKey(commitTypeGroup)) {
            groupedComments.put(commitTypeGroup, [])
          }
          groupedComments.get(commitTypeGroup).add(comment)
        }
      }
    }
    groupedComments
  }

  static semVerIncrement(Set<CommitTypeGroups> commitTypeGroups) {
    def incrementType = null
    commitTypeGroups.each {
      if (incrementType == null || it.incrementType < incrementType) {
        incrementType = it.incrementType
      }
    }
    incrementType
  }

  static findSemVerIncrement(List<String> commitComments) {
    def groupedComments = groupCommitComments(commitComments)
    def incrementType = semVerIncrement(groupedComments.keySet())
    if (incrementType == null) {
      throw new Exception('No conventional commits found')
    }
    incrementType
  }

  static private boolean isCommentInGroup(String comment, CommitTypeGroups commitTypeGroup) {
    def firstLine = comment.split('\\n')[0]
    commitTypeGroup.conventionalCommitTypes.any { type ->
      firstLine.toLowerCase().startsWith("${type.toLowerCase()}:")
    }
  }
}
