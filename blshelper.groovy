
PRODUCT_LOCATION = "D:\\chartis\\hg\\team_work\\claims\\claims-auto\\claims-configuration-auto\\src\\main\\resources\\product"

final TEST_PATH = "\\PREMIER_CLAIM_AU_1.0_claimsEvaluation\\Rules\\ruleSet_v1"

final TEST_RULES_FILE = "\\AutoEvaluationFeature\\base-rules.xml"

//rulesDir = new File(PRODUCT_LOCATION + TEST_PATH)
//rulesDir.eachDir{println it.name}

def rules = extractComponentRules(PRODUCT_LOCATION + TEST_PATH + TEST_RULES_FILE)

rules.each {println it}

extractWorkspaceRules(PRODUCT_LOCATION + "\\PREMIER_CLAIM_AU_1.0_claimsEvaluation")

def extractWorkspaceRules(workspace) {
    def ruleIds = []
    new File(workspace).eachRecurse(FileType.FILES) { 
        if (it =~ /.*rules\.xml/) println it
    }
}

def extractComponentRules(componentFile) {
    def ruleIds = []
    new File(componentFile).text.eachMatch(~/\w{8}-\w{4}-\w{4}-\w{4}-\w{12}/) {ruleIds << it}
    ruleIds
}
