### midi生成模块

本模块使用了[mider项目](https://github.com/whiterasbk/mider)提供的工具，相关的`mider code语法`见原项目。

本模块并不支持原项目里某些`mider code`特性（包括不限于）:

```shell
midi: 是否仅上传 midi 文件, 可选  
img: 是否仅上传 png 格式的乐谱  
pdf: 是否仅上传 pdf 文件, 可选  
mscz: 是否仅上传 mscz 文件, 可选  
```

## 本模块支持的示例

```
1. 小星星
>g>1155665  4433221  5544332  5544332
等同于
>g>ccggaag+ffeeddc+ggffeed+ggffeed
等同于
>g>c~g~^~v+f~v~v~v+(repeat 2:g~v~v~v+) (酌情使用

2. KFC 可达鸭
>g;bE>g^m+C-wmD+D^m+G-wmE+D^w+C-wmD+DvagaC

3. 碎月 
>85b>F+^$BC6GFG C$E F D$ED$b C+ g$b C$E F$E F+ F$E F$B G++ G$B C6C6$B C6 G+ G$E FGF$E C+ C$b C+C$EF$EFG $E
等同于
>85b;Cmin>F+^BC6GFG CE F DEDb C+ gb CE FE F+ FE FB G++ GB C6C6B C6 G+ GE FGFE C+ Cb C+CEFEFG E

4. 生日快乐
>88b>d.d- e+v g+ #f++ d.d- e+v a+ v+ d.d- D+b+g+ #f+ e+ C.C- b+ g+^ v+

5. 茉莉花
>110b>e+em^m~wv+g^v++e+em^m~wv+g^v++g+~~em^+av~++e+d^m+evv+c^v++evvmv+.eg+amg++d+egd^cwv++ ^-c+d+.ec^vwv++

6. bad apple!
>100b>e#fgab+ ED b+ e+ b a-- B-- A- g#f e#fga b+ ag #fe#fg #f--G--#F-e #d#f e#fgab+ ED b+e+ ba--B--A- g#f e#fgab+ ag

7. Jingle Bells
>100b>E~~+E~~+EmC^^++F~~+Fv~+Ev~^ D+G+E~~+E~~+EmC^^++F~~+Fv~~m~vDv++

8. 两只老虎 卡农
>g;3>(def tiger:1231 1231 3450 3450 5-6-5-4-31 5-6-5-4-31 15!10 15!10)
>g;4>00(=tiger)
>g;5>0000(=tiger)
>g;6>000000(=tiger)
>g;7>00000000(=tiger)
```

本模块对于`mider项目`仅是简单使用，若想得到更复杂的midi功能， 可使用[MiraiMidiProduce插件](https://github.com/whiterasbk/MiraiMidiProduce)。更多示例见 [awesome-melody](https://github.com/whiterasbk/MiraiMidiProduce/tree/master/awesome-melody)

#### 【指令】midi生成

**<子指令>: midi**  
**<指令参数>: 一段mider code**

> -> /<角色名> midi >g>ccggaag+ffeeddc+ggffeed+ggffeed  
> <- [语音]

当mider code中存在空格或多行时，需要加上`--`告知mirai-console将全文作为一个完整参数。详见[mirai-console 文本参数转义](https://github.com/mamoe/mirai/blob/v2.12.0/mirai-console/docs/Commands.md#%E6%96%87%E6%9C%AC%E5%8F%82%E6%95%B0%E8%BD%AC%E4%B9%89)。

> -> /<角色名> midi -- >g>1155665  4433221  5544332  5544332  
> <- [语音]
