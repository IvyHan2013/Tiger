Offical 内是官方给出的测试程序
        Good是正确的程序 生成的同名的.abs 和 .ir 文件 分别含有抽象语法分析树 和IR树&规范化的IR树&生成的“汇编代码”
        Bad 是错误的程序 将错误输出到屏幕上
Driver 是整体的入口 运行时调用tiger.Main.Main
Parser 是parse模块入口，运行时调用tiger.parse.Runable



tiger.parse.LexerTest 测试词法分析
tiger.parse.CupTest 测试语法分析
tiger.Semant.SemantTest 测试语义分析
tiger.Codegen.CodegenTest 测试代码生成
******************************************************************
韩雨桐
5130309482
ivyhan2013@126.com