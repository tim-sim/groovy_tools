import groovy.io.FileType

parseCommandLine(args)

def parseCommandLine(args) {
    def cli = new CliBuilder(usage: 'blshelper -[fght] [product_dir]', header: 'options:') 
    cli.with {
        h longOpt: 'help', 'Show usage information'
        f longOpt: 'fix', 'Try to fix BLS errors'
        g longOpt: 'generate', 'Re-generate rule set files'
        t longOpt: 'test', 'Check BLS rules for consistency'
    }
    def options = cli.parse(args)
    if (!options) {
        print 'Unknown error' 
    } else if (options.h) {
        println cli.usage() 
    } else if (options.t) {
        println 'Testing.. '
        def productDir = '.'
        def product = new Product(root: productDir)
        product.test()
        if (!product.errors) {
            println 'FAILED'
            product.errors.each { println it }
        } else {
            println 'OK'
        }
    } else {
        println cli.usage()
    }
}

class Product {
    def REGEX_RULE_ID = ~/\w{8}-\w{4}-\w{4}-\w{4}-\w{12}/
    def REGEX_RULE_FILE = /.*rules\.xml/
    def PATH_RULES = "\\Rules\\ruleSet_v1"
    def FILE_RULESET = "\\ruleSet.xml"
    def WORKSPACE_LIST = ['claimsEvaluation', 'claimsSummaryInfo']

    def logging = false
    def root
    def errors = []

    def test() {
        log 'Called test()'
        getRuleDirs().each {
            def ruleset = extractRules(new File(it + FILE_RULESET))
            def rules = extractComponentRules(new File(it))
            compareRules(ruleset, rules)
        }
    }
    
    def compareRules(ruleSet, rules) {
        log 'Called compareRules()'
        ruleSet.each {
            if (!rules.contains(it)) {
                errors << 'Component rule is not exist in ruleset: ' + it 
            }
        }
        rules.each {
            if (!ruleSet.contains(it)) {
                errors << 'Ruleset contains non-existent rule: ' + it
            }
        }
    }

    def generateRuleSet() {
    }

    def fixRuleSet() {
    }

    def getRuleDirs() {
        log 'Called getRuleDirs()'
        def dirs = []
        new File(root).eachDir { dirs << root + "\\" + it.name + PATH_RULES }
        log dirs
        dirs
    }

    def extractRules(file) {
        log "Called extractRules($file)"
        def rules = []
        file.text.eachMatch(REGEX_RULE_ID) { rules << it }
        log rules
        rules
    }

    def extractComponentRules(dir) {
        log "Called extractComponentRules($dir)"
        def rules = []
        dir.eachFileRecurse(FileType.FILES) {
            if (it =~ REGEX_RULE_FILE) { rules << extractRules(it) } 
        }
        log rules
        rules
    }

    def log(obj) {
        if (!logging) {
            return
        }
        if (obj == null) {
            println 'null'
        } else if (obj instanceof Collection) {
            obj.each { println it }
        } else {
            println obj
        }
    }
}
