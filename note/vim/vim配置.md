

# vim配置



### 一、[Vundle](https://github.com/VundleVim/Vundle.vim)

Vundle是一个vim插件管理器

1. ##### 安装Vundle

   `git clone https://github.com/VundleVim/Vundle.vim.git ~/.vim/bundle/Vundle.vim`

2. ##### 配置Vundle

   在.vimrc顶部加入以下代码。

   ```vim
   set nocompatible              " be iMproved, required
   filetype off                  " required
   
   " set the runtime path to include Vundle and initialize
   set rtp+=~/.vim/bundle/Vundle.vim
   call vundle#begin()
   " alternatively, pass a path where Vundle should install plugins
   "call vundle#begin('~/some/path/here')
   
   " let Vundle manage Vundle, required
   Plugin 'VundleVim/Vundle.vim'
   
   
   " All of your Plugins must be added before the following line
   call vundle#end()            " required
   filetype plugin indent on    " required
   " To ignore plugin indent changes, instead use:
   " filetype plugin on
   "
   " Brief help
   " :PluginList       - lists configured plugins
   " :PluginInstall    - installs plugins; append `!` to update or just :PluginUpdate
   " :PluginSearch foo - searches for foo; append `!` to refresh local cache
   " :PluginClean      - confirms removal of unused plugins; append `!` to auto-approve removal
   "
   " see :h vundle for more details or wiki for FAQ
   " Put your non-Plugin stuff after this line
   ```

3. ##### 安装插件

   在`call vundle#begin()`和`call vundle#end()`之间添加

   `Plugin 'plugin name'(要添加的插件名称或url)`

   两种方法安装：

   * 打开`vim`，运行`:PluginInstall`
   * 直接在shell命令行中输入`vim + PluginInstall + qall`

4. ##### 更新插件

   打开`vim`，运行`:PluginUpdate`

   **在安装或者更新后，按'u'可以查看更新日志，按'l'可以查看日志以便查看是否发生错误**

5. ##### 删除插件

   在`.vimrc`文件中删除想要删除插件的行，例如：~~Plugin 'tpope/vim-fugitive~~，

   之后打开`vim`，运行`:PluginClean!`，

   会自动清除不在`.vimrc`中的插件



二、


