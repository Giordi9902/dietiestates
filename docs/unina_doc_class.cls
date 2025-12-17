\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{unina_doc_class}[2023/09/14 Unina Doc Class]
\LoadClass[10pt,twoside,fleqn]{extreport}

% Requirements

% Requirements
\RequirePackage[svgnames]{xcolor}                     % Colors with svg names
\RequirePackage[hidelinks]{hyperref} 
\RequirePackage{wrapfig}
\RequirePackage{svg}
\RequirePackage{epigraph}
\RequirePackage{graphicx}                             % Load images
\RequirePackage{geometry}  
\RequirePackage{tikz-cd}                           % Page layout
\RequirePackage{tikz}        
\RequirePackage{pdfpages}                         % Drawings
\RequirePackage{forest}                               % Pretty obv?
\RequirePackage{afterpage}                            % Macro to run after the page has ended
\RequirePackage{fancyhdr}                             % Page headers and footers
\RequirePackage{mathtools}                            % Math symbols
\RequirePackage[no-math]{fontspec}                    % font family
\RequirePackage[explicit]{titlesec}                   % Change document style
\RequirePackage{titletoc}                             % Change document style
\RequirePackage{etoolbox}                             % Patch environments
\RequirePackage[italian]{babel}                       % Change document lang to it
\RequirePackage{hyphenat}                             % Hypernat words that requires to
\RequirePackage{parskip}                              % Change parskip/parindent
\RequirePackage{listings, lstautogobble, lstfiracode} % lstlisting for code, autogobble and firacode for good looking
                 % Add urls
\usepackage{amsmath, amsthm,thmtools, amssymb, fdsymbol, amsfonts, mathtools}           % Symbols
\RequirePackage{enumitem}                             % Customize itemize/enumerate envs
\RequirePackage{calc}                                 % latex calculations
\RequirePackage{tabularray}                           % Tables. For more info see: https://ctan.mirror.garr.it/mirrors/ctan/macros/latex/contrib/tabularray/tabularray.pdf
\RequirePackage[most, many, breakable]{tcolorbox}     % Colored and framed text boxes with a heading line. For more info see: https://www.tug.org/docs/latex/tcolorbox/tcolorbox.pdf
\RequirePackage{luacode}                              % Enable lua commands
\RequirePackage{xparse}                               % Value parsing
\RequirePackage{cancel}                               % Place lines through maths formulae
\RequirePackage[object=vectorian]{pgfornament}        % Decorations
\RequirePackage{dashrule}                             % Rules for dashed line
\RequirePackage{float}
\RequirePackage{imakeidx} 
\RequirePackage{hyperref}                         % Support for the inclusion of small, ‘sub’, ﬁgures and tables
\RequirePackage{shellesc}                             % Support for shell commands
\RequirePackage{polynom}
\RequirePackage{bytefield}
\RequirePackage{caption}
\RequirePackage{subfig}
\makeindex[title=Indice Alfabetico,columns=2,intoc,options={-s index.ist}]
% Allow also subsubsection to be numbered
\setcounter{secnumdepth}{3}

% Long Tables Caption
\DefTblrTemplate{contfoot-text}{default}{Continua nella pagina successiva}
\DefTblrTemplate{conthead-text}{default}{(Continuazione)}

% Macros
\newcommand\blankpage{\null\thispagestyle{empty}\addtocounter{page}{-1}\newpage}

% Define colors
\definecolor{orange}{HTML}{FF9507}
\definecolor{blue}{HTML}{179AFF}
\definecolor{sapphire}{HTML}{2B50AA}
\definecolor{cherryred}{HTML}{FF004D}
\definecolor{turquoise}{HTML}{00F5CC}
\definecolor{seagreen}{HTML}{22AAA1}
\definecolor{green}{HTML}{17FF7C}
\definecolor{red}{HTML}{FF3E4B}
\definecolor{yellow}{HTML}{DBED00}
\definecolor{naplesyellow}{HTML}{FFDF64}
\definecolor{violet}{HTML}{47007D}
\definecolor{pink}{HTML}{FF69B4}
\definecolor{neutral}{HTML}{888888}

% Azure
\definecolor{azure-gradient-1}{HTML}{B1E7E1}
\definecolor{azure-gradient-2}{HTML}{A1E2DB}
\definecolor{azure-gradient-3}{HTML}{92DDD6}
\definecolor{azure-gradient-4}{HTML}{83D8CF}
\definecolor{azure-gradient-5}{HTML}{73D3C9}
\definecolor{azure-gradient-6}{HTML}{64CEC3}

% Pink
\definecolor{pink-gradient-1}{HTML}{ECD7D5}
\definecolor{pink-gradient-2}{HTML}{E6C9C7}
\definecolor{pink-gradient-3}{HTML}{DFBBB9}
\definecolor{pink-gradient-4}{HTML}{D8ADAB}
\definecolor{pink-gradient-5}{HTML}{D2A09D}
\definecolor{pink-gradient-6}{HTML}{CC928F}

% Purple
\definecolor{purple-gradient-1}{HTML}{C5C2EB}
\definecolor{purple-gradient-2}{HTML}{B5B2E6}
\definecolor{purple-gradient-3}{HTML}{A6A3E1}
\definecolor{purple-gradient-4}{HTML}{9793DC}
\definecolor{purple-gradient-5}{HTML}{8884D7}
\definecolor{purple-gradient-6}{HTML}{7974D2}

% Yellow
\definecolor{yellow-gradient-1}{HTML}{EDE8AB}
\definecolor{yellow-gradient-2}{HTML}{EAE39A}
\definecolor{yellow-gradient-3}{HTML}{EAE39A}
\definecolor{yellow-gradient-4}{HTML}{E3DA78}
\definecolor{yellow-gradient-5}{HTML}{E0D667}
\definecolor{yellow-gradient-6}{HTML}{DCD156}

% Red
\definecolor{red-gradient-1}{HTML}{FF7073}
\definecolor{red-gradient-2}{HTML}{FF5C5F}
\definecolor{red-gradient-3}{HTML}{FF474A}
\definecolor{red-gradient-4}{HTML}{FF3336}
\definecolor{red-gradient-5}{HTML}{FF1F22}
\definecolor{red-gradient-6}{HTML}{FF0A0E}

% Orange
\definecolor{orange-gradient-1}{HTML}{FABE75}
\definecolor{orange-gradient-2}{HTML}{F9B562}
\definecolor{orange-gradient-3}{HTML}{F9AC4E}
\definecolor{orange-gradient-4}{HTML}{F8A23A}
\definecolor{orange-gradient-5}{HTML}{F79926}
\definecolor{orange-gradient-6}{HTML}{F69013}

% Brown
\definecolor{brown-gradient-1}{HTML}{BCA576}
\definecolor{brown-gradient-2}{HTML}{B59C69}
\definecolor{brown-gradient-3}{HTML}{AE925B}
\definecolor{brown-gradient-4}{HTML}{A48851}
\definecolor{brown-gradient-5}{HTML}{967D4A}
\definecolor{brown-gradient-6}{HTML}{897243}

% Green
\definecolor{green-gradient-1}{HTML}{D4E5B3}
\definecolor{green-gradient-2}{HTML}{CCE0A4}
\definecolor{green-gradient-3}{HTML}{C3DB95}
\definecolor{green-gradient-4}{HTML}{B9D585}
\definecolor{green-gradient-5}{HTML}{B1D076}
\definecolor{green-gradient-6}{HTML}{A8CB67}

% Color palette
\colorlet{primary}{sapphire}
\colorlet{accent}{violet}


% Icon definitions

\makeatletter
        \newfontfamily\IcoMoon{IcoMoon}[
                Path = fonts/,
                Extension = .ttf,
                UprightFont = *,
        ]

        \def\inumber{{\IcoMoon\char"E918}}
        \def\inumbernegative{{\IcoMoon\char"E919}}
        \def\itick{{\IcoMoon\char"E915}}
        \def\icross{{\IcoMoon\char"E916}}
        \def\ihome{{\IcoMoon\char"E914}}
        \def\ifolder{{\IcoMoon\char"E92F}}
        \def\iquotel{{\IcoMoon\char"E977}}
        \def\iquoter{{\IcoMoon\char"E978}}
        \def\iwrench{{\IcoMoon\char"E991}}
        \def\ieqh{{\IcoMoon\char"E992}}
        \def\ieqv{{\IcoMoon\char"E993}}
        \def\icog{{\IcoMoon\char"E994}}
        \def\icogs{{\IcoMoon\char"E995}}
        \def\istats{{\IcoMoon\char"E99B}}
        \def\iwinnerflag{{\IcoMoon\char"E9CC}}
        \def\iwarning{{\IcoMoon\char"EA07}}
        \def\inotification{{\IcoMoon\char"EA08}}
        \def\iquestion{{\IcoMoon\char"EA09}}
        \def\iinfo{{\IcoMoon\char"EA0C}}
        \def\icancel{{\IcoMoon\char"EA0D}}
        \def\ialphaaasc{{\IcoMoon\char"EA48}}
        \def\ialphaddesc{{\IcoMoon\char"EA49}}
        \def\inumberasc{{\IcoMoon\char"EA4A}}
        \def\inumberdesc{{\IcoMoon\char"EA4B}}
        \def\iorderasc{{\IcoMoon\char"EA4C}}
        \def\iorderdesc{{\IcoMoon\char"EA4D}}
        \def\iboxdone{{\IcoMoon\char"EA52}}
        \def\iboxundone{{\IcoMoon\char"EA53}}
        \def\iradiodone{{\IcoMoon\char"EA54}}
        \def\iradiodonenegative{{\IcoMoon\char"EA55}}
        \def\iradioundone{{\IcoMoon\char"EA56}}
        \def\isuperscript{{\IcoMoon\char"EA69}}
        \def\icodeopentag{{\IcoMoon\char"EA7F}}
        \def\icodeclosetag{{\IcoMoon\char"EA80}}
        \def\iterminal{{\IcoMoon\char"EA81}}
        \def\imail{{\IcoMoon\char"E900}}
        \def\iwindow{{\IcoMoon\char"E906}}
        \def\icpu{{\IcoMoon\char"E907}}
        \def\ieye{{\IcoMoon\char"E908}}
        \def\icomplexcox{{\IcoMoon\char"E901}}
        \def\ibug{{\IcoMoon\char"E902}}
        \def\iattachment{{\IcoMoon\char"E909}}
        \def\ilabel{{\IcoMoon\char"E90A}}
        \def\ibookmark{{\IcoMoon\char"E90B}}
        \def\ibadge{{\IcoMoon\char"E90C}}
        \def\iuser{{\IcoMoon\char"E903}}
        \def\ibusinessman{{\IcoMoon\char"E904}}
        \def\ibusinesswoman{{\IcoMoon\char"E905}}
        \def\iidea{{\IcoMoon\char"E90D}}
        \def\isettings{{\IcoMoon\char"E90E}}
        \def\ifatinfo{{\IcoMoon\char"E90F}}
        \def\iclock{{\IcoMoon\char"E910}}
        \def\itimer{{\IcoMoon\char"E911}}
        \def\iflag{{\IcoMoon\char"E912}}
        \def\iwinnercup{{\IcoMoon\char"E913}}
        \def\igraduate{{\IcoMoon\char"E917}}
        \def\igithub{{\IcoMoon\char"EAB0}}


        \newfontfamily\IcoFont{IcoFont}[
                Path = fonts/,
                Extension = .ttf,
                UprightFont = *,
        ]

        \def\imail{{\IcoFont\char"EF12}}
        \def\igraduate{{\IcoFont\char"EADB}}
        \def\iid{{\IcoFont\char"EF4A}}
        \def\isettings{{\IcoFont\char"EFE1}}
        \def\isettingsalt{{\IcoFont\char"EC83}}
        \def\iterminalalt{{\IcoFont\char"F00A}}
        \def\iauto{{\IcoFont\char"EEA9}}
        \def\ifile{{\IcoFont\char"EB08}}
        \def\ifilecode{{\IcoFont\char"EB0C}}
        \def\ibook{{\IcoFont\char"EAD1}}
        \def\ibookmark{{\IcoFont\char"EEC0}}
        \makeatother
        % Define fonts
        \makeatletter
        \newfontfamily\Lato{Lato}[
                Path = fonts/,
                Extension = .ttf,
                UprightFont = *-Regular,
                BoldFont = *-Bold,
                ItalicFont = *-Italic,
                BoldItalicFont = *-BoldItalic
        ]

        \newfontfamily\Scientifica{scientifica}[
                Path = fonts/,
                Extension = .ttf,
                UprightFont = *,
                BoldFont = *Bold,
                ItalicFont = *Italic,
        ]

        \newfontfamily\FiraCode{FiraCode}[
                Path = fonts/,
                Extension = .ttf,
                UprightFont = *-Regular,
                BoldFont = *-Bold,
                Contextuals=Alternate
        ]

        \newfontfamily\LatoThin{Lato-Thin}[
                Path = fonts/,
                Extension = .ttf,
                UprightFont = *,
                ItalicFont = *Italic,
        ]

        \newfontfamily\LatoLight{Lato-Light}[
                Path = fonts/,
                Extension = .ttf,
                UprightFont = *,
                ItalicFont = *Italic,
        ]

        \newfontfamily\LatoBlack{Lato-Black}[
                Path = fonts/,
                Extension = .ttf,
                UprightFont = *,
                ItalicFont = *Italic,
        ]

        \newfontfamily\Karla{Karla}[
                Path = fonts/,
                Extension = .ttf,
                UprightFont = *-Regular,
                BoldFont = *-Bold,
                ItalicFont = *-Italic,
                BoldItalicFont = *-BoldItalic
        ]

        \newfontfamily\Agency{Agency}[
                Path = fonts/,
                Extension = .ttf,
                UprightFont = *,
        ]

        \newfontfamily\Alte{AlteDIN1451Mittelschrift}[
                Path = fonts/,
                Extension = .ttf,
                UprightFont = *,
                BoldFont=*,
                ItalicFont=*,
                BoldItalicFont=*,
                BoldFeatures={FakeBold=2},
                ItalicFeatures={FakeSlant=0.25},
                BoldItalicFeatures={FakeBold=2, FakeSlant=0.25},
        ]

        \newfontfamily\AlteG{AlteDIN1451MittelschriftG}[
                Path = fonts/,
                Extension = .ttf,
                UprightFont = *,
                BoldFont=*,
                ItalicFont=*,
                BoldItalicFont=*,
                BoldFeatures={FakeBold=2},
                ItalicFeatures={FakeSlant=0.25},
                BoldItalicFeatures={FakeBold=2, FakeSlant=0.25},
        ]

        \newfontfamily\Gobold{Gobold}[
                Path = fonts/,
                Extension = .otf,
                UprightFont = *-Regular,
                BoldFont=*-Bold,
                ItalicFont=*-Italic,
                BoldItalicFont=*-BoldItalic,
        ]

        \newfontfamily\GoboldHollow{GoboldHollow}[
                Path = fonts/,
                Extension = .otf,
                UprightFont = *-Regular,
                BoldFont=*-Bold,
                ItalicFont=*-Italic,
                BoldItalicFont=*-BoldItalic,
        ]
\makeatother

%% Document Style
\makeatletter
        %% Global
        \setlength\parindent{0pt}
        \setlength\parskip{0.75em}
        \setlength{\headsep}{2cm}

        %% Define no Par Skip
        \let\svpar\par
        \edef\svparskip{\the\parskip}
        \def\revertpar{\svpar\setlength\parskip{\svparskip}\let\par\svpar}
        \def\noparskip{\leavevmode\setlength\parskip{0pt}\def\par{\svpar\let\par\revertpar}}

        %% Chapter
        \setcounter{chapter}{0}
        \def\currentchapter{}
        \titlespacing*{\chapter}{0pt}{-10pt}{30pt}

        \titleformat{\chapter}[frame]
                {\setlength{\fboxrule}{5pt}\color{primary!85!black}}
                {\hspace{0.25cm}\fontsize{160}{0}\selectfont\AlteG \textcolor{primary!20!white}{\ibookmark}\hspace{0.25cm}}
                {1cm}
                {\color{primary}\fontsize{30}{42}\selectfont\Gobold\centering #1\global\def\currentchapter{#1}}
                %{\color{primary}\fontsize{30}{42}\selectfont\Gobold\centering\chaptertitlename: #1\global\def\currentchapter{#1}}

        \titleformat{name=\chapter,numberless}[frame]
                {\setlength{\fboxrule}{5pt}\color{primary!85!black}}
                {}
                {1cm}
                {\color{primary}\fontsize{30}{42}\selectfont\Gobold\centering #1\global\def\currentchapter{#1}}

        %% Table of Contents
        \titlecontents*{chapter}
                [0pt]
                {\addvspace{0.5cm}}
                {\color{primary!60!black}\fontsize{12}{0}\selectfont\Gobold\thecontentslabel\hspace{0.125cm}\color{primary}}
                {}
                {\color{primary!80!black}\hfill\bfseries\Scientifica\fontsize{12}{0}\selectfont\contentspage\hrule height 1pt width \textwidth\relax}

        \titlecontents{section}
                [0.5cm]
                {}
                {\color{primary!60!black}\fontsize{10}{0}\selectfont\AlteG\thecontentslabel\hspace{0.125cm}\color{primary}}
                {}
                {\color{primary!80!black}\hfill\bfseries\Scientifica\fontsize{10}{0}\selectfont\contentspage}

        \titlecontents{subsection}
                [1cm]
                {}
                {\color{primary!60!black}\fontsize{8}{0}\selectfont\Lato\thecontentslabel\hspace{0.125cm}\color{primary}}
                {}
                {\color{primary!80!black}\hfill\bfseries\Scientifica\fontsize{8}{0}\selectfont\contentspage}

        \titlecontents{subsubsection}
                [1cm]
                {}
                {\color{primary!60!black}\fontsize{7}{0}\selectfont\Lato\thecontentslabel\hspace{0.125cm}\color{primary}}
                {}
                {\color{primary!80!black}\hfill\bfseries\Scientifica\fontsize{7}{0}\selectfont\contentspage}

        %% Section
        \titlespacing*{\section}{0pt}{6mm}{6mm}
        \titleformat{\section}[hang]
        {}
        {\rlap{\color{primary!85!black}\rule[-6pt]{\textwidth}{1.2pt}}\colorbox{primary!85!black}{%
                \raisebox{0pt}[13pt][3pt]{ \makebox[60pt]{% height, width
                                        \fontsize{15}{12}\AlteG\color{white}{\thesection}}
                        }}}%
        {15pt}%
        {\color{primary}\fontsize{20}{12}\selectfont\Gobold#1\hspace{\fill}\ibook}

        %% Subsection
        \titlespacing*{\subsection}{0pt}{6mm}{6mm}
        \titleformat{\subsection}[hang]
                {\color{primary!20!black}\fontsize{12}{12}\selectfont\Lato}
                {\thesubsection\fontsize{8}{6}\selectfont\ibookmark\hspace{10pt}}
                {0pt}
                {\color{accent!65!black}#1}

        \titlespacing*{\subsubsection}{0pt}{4mm}{4mm}
        \titleformat{\subsubsection}[hang]
                {\color{primary!20!black}\fontsize{10}{10}\selectfont\Lato}
                {\thesubsubsection\fontsize{8}{6}\selectfont\ibookmark\hspace{9pt}}
                {0pt}
                {\color{accent!65!black}#1}

        %% Abstract
        \renewenvironment{abstract}
                {\thispagestyle{empty}\vfill{\par\raggedleft\fontsize{16}{18}\selectfont\Gobold\color{primary!75!black} Estratto\par\vspace{0.25cm}\hrule height 2pt \relax\par\noparskip}%
                \setlength{\parskip}{0.5em}\color{primary!35!black}\fontsize{12}{10}\selectfont\Lato}
                {\newpage}


        %% Highlight
        \def\hi#1{{\color{accent!80!black}\LatoBlack\mbox{#1}}}
        \def\dir#1{{\color{primary!80!black}\Mononoki\mbox{\penalty 100\ifolder\hspace{1pt}#1}}}
        \def\file#1{{\color{accent!80!white}\Mononoki\mbox{\penalty 100\ifile\hspace{1pt}#1}}}

        %% Scale text size using current font size
        \def\t@scale#1{\fontsize{#1\dimexpr\f@size pt\relax}{\dimexpr\f@size pt\relax}\selectfont}
        \def\t@apex@measure#1#2{\raisebox{#2\dimexpr\f@size pt\relax}{\t@scale{#2}#1}}
        \def\t@apex#1{\t@apex@measure{#1}{0.65}}
        \def\sp@text#1#2{{#1\t@scale{0.7}\t@apex{#2}}}
        \def\sp@rtext#1#2{{\t@scale{0.7}\t@apex{#2}}{#1}}

        \def\dir#1{{\color{primary!75!black}\sp@rtext{#1}{\ifolder}}}
        \def\qmake{\sp@text{gnu make}{\iauto}}
        \def\qmakefile{\sp@text{makefile}{\ifile\iauto}}
        \def\qterm{{\color{primary}\sp@rtext{terminal emulator}{\iterminal}}\ }
        \def\qstruct#1{{\color{primary}\t@apex@measure{struct}{0.5}\sp@text{#1}{\iwrench}}}
        \def\qfunction#1{{\color{cherryred}\sp@text{#1}{\ifile\icodeopentag}}}

        \newcommand{\mycbox}[1]{\tikz{\path[draw=#1,fill=#1] (0,0) rectangle (1cm,1cm);}}

        \newcommand{\separator}{%
          \noindent
          \begin{center}
            {\color{DarkViolet}
              \resizebox{0.5\linewidth}{1ex}
                        {{%
                            {\begin{tikzpicture}
                                \node  (C) at (0,0) {};
                                \node (D) at (9,0) {};
                                \path (C) to [ornament=85] (D);
            \end{tikzpicture}}}}}%
          \end{center}
        }

        %% \shapeableseparator{<color>}{<ornament>}
        \newcommand{\shapeableseparator}[2]{%
          \nointerlineskip\vspace{.5\baselineskip}\hspace{\fill}
                           {\color{#1}
                             \resizebox{0.5\linewidth}{2ex}
                                       {{%
                                           {\begin{tikzpicture}
                                               \node  (C) at (0,0) {};
                                               \node (D) at (9,0) {};
                                               \path (C) to [ornament=#2] (D);
                           \end{tikzpicture}}}}}%
                           \hspace{\fill}
                           \par\nointerlineskip\vspace{.5\baselineskip}
        }


        %% Xparse commands
        \NewDocumentCommand{\optional}{o}{
          \IfValueTF{#1}{%
            #1
          }{%
            % \phantom{TEXT}
          }%
        }

        \fancypagestyle{plain}{
          \fancyhf{}
          \fancyhead[L]{}
          \fancyhead[C]{}
          \fancyhead[R]{}
          \renewcommand{\headrule}{}

          % TODO: Pass the name of the document as a Variable
          \fancyfoot[L]{{\fontsize{10}{0}\color{primary}\documentname}}
          \fancyfoot[C]{{\fontsize{14}{0}{\color{primary}\thepage}}}
          \fancyfoot[R]{{\fontsize{10}{0}\color{primary}\currentchapter\hspace{2pt}\ibook}}
          \renewcommand{\footrule}{{\color{primary}\hrule height 1pt width \textwidth \relax}}
        }

\makeatother

% Define page layout(s)
\geometry{ a4paper,
  margin = 7.5mm,
  bottom = 12mm,
  footskip = 10mm,
  heightrounded,
  nohead,
  nomarginpar }

\overfullrule=5pt

% Tikz
\usetikzlibrary{calc, arrows, shapes.geometric, positioning, automata}

\tikzset{
        mpath/.style={
                fill=#1,
                draw=#1,
                line width=0.1pt
        },
        square/.style={regular polygon,regular polygon sides=4, inner sep=0pt, outer sep=0pt},
}

% \coverpage{<SIZE-OF-DOCUMENT-YEAR>}{<DOCUMENT-YEAR>}{<SIZE-OF-DOCUMENT-NAME>}{<DOCUMENT-NAME>}
\newcommand\coverpage[4]{%
        \newgeometry{noheadfoot=true, margin=0pt}\newpage\thispagestyle{empty}%
        \noindent\begin{tikzpicture}[remember picture, overlay, shift=(current page.south west)]%
                \begin{scope}[x={(current page.south east)},y={(current page.north west)}, overlay]
                        % Background
                        \fill [primary!75!white] (0,0) rectangle (1,1);

                        %% Layout
                        % Text box
                        \path [mpath=primary!25!white]  (0,0.25) -- (0.2,0.4) -- (0.8, 0.4) -- (1, 0.55) -- (1, 0.1) -- (0.93, 0.05) -- (0.07, 0.05) -- (0,0) -- cycle;
                        % Left Wing
                        \path [mpath=primary!85!white]	(0,0.27) -- (0.17,0.40) -- (0.2, 0.40) -- (0,0.25) -- cycle;
                        % Right Wing
                        \path [mpath=primary!85!white]	(0.77,0.4) -- (0.8, 0.40) -- (1, 0.55) -- (1, 0.57) -- cycle;
                        % Under Wing
                        \path [mpath=primary!65!black]  (0,0) -- (0.025,0) -- (0.075, 0.035) -- (0.94, 0.035) -- (1, 0.08) -- (1, 0.1) -- (0.93, 0.05) -- (0.07, 0.05) -- (0, 0) -- cycle;
                        % Central Wing
                        \path [mpath=primary!35!black]	(0,0.27) -- (0.17,0.40) -- (0.77, 0.40) -- (1, 0.57) -- (1, 0.8) -- (0.85, 0.8) -- (0.6, 0.6) -- (0.13, 0.6) -- (0,0.5) -- cycle;

                        %% Text nodes
                        % Icon & University
                        \node (fiisymbol) at ([yshift=-0.5cm, xshift=0.5cm]0,1)[align=center, anchor=north west, inner sep=0pt, outer sep=0pt]
                                { \color{primary!65!black}\includegraphics[%
                                                width=3cm,%
                                                height=3cm,%
                                                keepaspectratio%
                                        ] {sources/uni_logo.pdf}};
                        \node (fiitext) at ([xshift=0.25cm] fiisymbol.north east) [align=left, anchor=north west]
                                {{\color{primary!65!black}\Lato\fontsize{10}{0}\selectfont Università degli Studi di Napoli}};
                        \node (fiiname) at ([xshift=0.01cm, yshift=0.2cm]fiitext.south west) [align=left, anchor=north west]
                                {{\color{primary!65!black}\Agency\fontsize{24}{0}\selectfont Federico II}};

                        % Date and academic year
                        \node (academicyear) at ([xshift=-0.5cm] fiitext.north -| 1,1) [align=right, anchor=north east]
                                {{\color{primary!65!black}\Gobold\fontsize{#1}{0}\selectfont #2}};

                        % Title of the documentation
                        \node at ([xshift=0.1cm, yshift=0.1cm] $(0,0)!0.5!(1,1)$) [align=center, anchor=center, inner sep=0pt, outer sep=0pt]
                                {{\color{primary!70!white}\Gobold\fontsize{#3}{0}\selectfont #4}};
                \end{scope}%
        \end{tikzpicture}\restoregeometry\newpage}

% \introduction{Professor Name}{Additional Text}
\newcommand\introduction[2]{
  \begin{abstract}
      Il presente documento è una raccolta di appunti accuratamente riorganizzati e arricchiti da un layout grafico migliorato, basato sul corso di \documentname\ tenuto \ifx \tutorgender\undefined dal \else dalla \fi docente #1 per il Corso di Laurea in Informatica \documentyear\ dell'Università Federico II di Napoli.
      
      \begin{center} #2 \end{center}
      
      Si precisa che questo documento non è destinato a sostituire libri di testo, appunti del docente o lezioni frontali, pur essendo basato su tali fonti. Il suo scopo principale è offrire una risorsa unificata per lo studio dell'intero programma del corso, integrando il materiale più complesso con approfondimenti, esempi ed esercizi pensati per chiarire dubbi, colmare lacune e facilitare il superamento delle difficoltà riscontrate durante lo studio individuale.
      
      Si ringrazia \href{https://github.com/luftmensch-luftmensch}{\hi{\iuser Valentino Bocchetti}}, curatore dell'aspetto grafico di questo documento, per il prezioso contributo.
      
      Un ringraziamento speciale va a tutti i revisori che hanno fornito consigli e proposte di modifica, migliorando ulteriormente la qualità del contenuto. Infine, si ringraziano i lettori per la loro attenzione e si invita chiunque riscontri errori o abbia suggerimenti a contattare gli autori, con l'augurio che questo lavoro possa essere di grande utilità per il loro percorso di studio.

      \ifx \xauthors \undefined
      \else
        \begin{flushright}
          \textit{Redazione a cura di} \xauthors
        \end{flushright}
      \fi

      \ifx \yauthors \undefined
      \else
        \begin{flushright}
          \textit{Revisione testo a cura di} \yauthors
        \end{flushright}
      \fi

      \ifx \zauthors \undefined
      \else
      \begin{flushright}
        \textit{Si ringraziano} \zauthors\space per il materiale fornito
      \end{flushright}
      \fi

      \begin{flushright}
        Documento aggiornato al \hi{\epoch} (Revisione documento \hi{\gitparse})
      \end{flushright}
  \end{abstract}
}


% Tcolorbox theorems
\tcbuselibrary{skins}

\makeatletter
\newtcbtheorem[number within=section]{Definition}{}{
  enhanced,breakable,
  before skip=5mm,after skip=5mm, colback=red!5,colframe=red!80!black,boxrule=0.5mm,
  attach boxed title to top left={xshift=1cm,yshift*=1mm-\tcboxedtitleheight},
  drop fuzzy shadow,
  boxed title style={
    frame code={
      \path[fill=tcbcolback]
      ([yshift=-1mm,xshift=-1mm]frame.north west)
      arc[start angle=0,end angle=180,radius=1mm]
      ([yshift=-1mm,xshift=1mm]frame.north east)
      arc[start angle=180,end angle=0,radius=1mm];
      \path[left color=tcbcolback!60!black,right color=tcbcolback!60!black,
        middle color=tcbcolback!80!black]
      ([xshift=-2mm]frame.north west) -- ([xshift=2mm]frame.north east)
           [rounded corners=1mm]-- ([xshift=1mm,yshift=-1mm]frame.north east)
           -- (frame.south east) -- (frame.south west)
           -- ([xshift=-1mm,yshift=-1mm]frame.north west)
           [sharp corners]-- cycle;
    },
    interior engine=empty,
  },
  fonttitle=\bfseries,
  label={#2},#1}{def}
\makeatother

\makeatletter
\newtcolorbox[number within=section]{infobox}[1]{%
	title=#1, fonttitle=\bfseries,
        drop fuzzy shadow,
	colback=green-gradient-1,enhanced,
	attach boxed title to top left={xshift=-5mm},
	boxrule=0pt,
	before skip=1cm, after skip=1cm, right skip=0cm,
	breakable,
	toprule=0pt, bottomrule=0pt, rightrule=0pt, leftrule=4pt,
	skin=enhancedlast jigsaw,colframe=green-gradient-6,colbacktitle=green-gradient-5,
	boxed title style = {
		frame code={
			\fill[green-gradient-5](frame.south west)--(frame.north west)--(frame.north east)--([xshift=3mm]frame.east)--(frame.south east)--cycle;
			\draw[line width=1mm,green-gradient-5]([xshift=2mm]frame.north east)--([xshift=5mm]frame.east)--([xshift=2mm]frame.south east);
			\draw[line width=1mm,green-gradient-5]([xshift=5mm]frame.north east)--([xshift=8mm]frame.east)--([xshift=5mm]frame.south east);
			\fill[green!40](frame.south west)--+(4mm,-2mm)--+(4mm,2mm)--cycle;
		}
	}
}
\makeatother


\makeatletter
\newtcolorbox[number within=section]{warningbox}[1]{%
	title=#1, fonttitle=\bfseries,
	colback=yellow-gradient-1,enhanced,
        drop fuzzy shadow,
	attach boxed title to top left={xshift=-5mm},
	boxrule=0pt,
	before skip=1cm, after skip=1cm, right skip=0cm,
	breakable,
	toprule=0pt, bottomrule=0pt, rightrule=0pt, leftrule=4pt,
	skin=enhancedlast jigsaw,colframe=yellow-gradient-6,colbacktitle=yellow-gradient-5,
	boxed title style = {
		frame code={
			\fill[yellow-gradient-5](frame.south west)--(frame.north west)--(frame.north east)--([xshift=3mm]frame.east)--(frame.south east)--cycle;
			\draw[line width=1mm,yellow-gradient-5]([xshift=2mm]frame.north east)--([xshift=5mm]frame.east)--([xshift=2mm]frame.south east);
			\draw[line width=1mm,yellow-gradient-5]([xshift=5mm]frame.north east)--([xshift=8mm]frame.east)--([xshift=5mm]frame.south east);
			\fill[yellow!40](frame.south west)--+(4mm,-2mm)--+(4mm,2mm)--cycle;
		}
	}
}
\makeatother

\makeatletter
\newtcolorbox[number within=section]{errorbox}[1]{%
	title=#1, fonttitle=\bfseries,
	colback=red-gradient-1,enhanced,
        drop fuzzy shadow,
	attach boxed title to top left={xshift=-5mm},
	boxrule=0pt,
	before skip=1cm, after skip=1cm, right skip=0cm,
	breakable,
	toprule=0pt, bottomrule=0pt, rightrule=0pt, leftrule=4pt,
	skin=enhancedlast jigsaw,colframe=red-gradient-6,colbacktitle=red-gradient-5,
	boxed title style = {
		frame code={
			\fill[red-gradient-5](frame.south west)--(frame.north west)--(frame.north east)--([xshift=3mm]frame.east)--(frame.south east)--cycle;
			\draw[line width=1mm,red-gradient-5]([xshift=2mm]frame.north east)--([xshift=5mm]frame.east)--([xshift=2mm]frame.south east);
			\draw[line width=1mm,red-gradient-5]([xshift=5mm]frame.north east)--([xshift=8mm]frame.east)--([xshift=5mm]frame.south east);
			\fill[red!40](frame.south west)--+(4mm,-2mm)--+(4mm,2mm)--cycle;
		}
	}
}
\makeatother

\makeatletter
\newtcolorbox{infoboxtag}[1]{
  enhanced,
  drop fuzzy shadow,
  before skip=7pt,
  after skip =7pt,
  left skip=0pt,
  right skip=0pt,
  boxrule=0.4pt,
  top=0pt,
  bottom=0pt,
  left=4mm,
  right=4mm,
  arc=1pt,
  boxsep=4pt,
  before upper={\vphantom{dlg}},
  colframe=green!50!black,
  coltext=green!25!black,
  colback=green!10!white,
  overlay={
    \begin{tcbclipinterior}
      \fill[green!75!blue!50!white]
      (frame.south west)
      rectangle      node[text=white,
        font=\sffamily\bfseries\tiny,
        %rectangle,
        %rotate=90
      ]
      {#1}
      ([xshift=4mm]frame.north west);
      \fill[green!75!blue!50!white]
      ([xshift=-4mm]frame.south east)
      rectangle      node[text=white,
        font=\sffamily\bfseries\tiny,
        %rectangle,
        %rotate=90
      ]
      {#1}
      (frame.north east);
    \end{tcbclipinterior};
  }
}
\makeatother

\makeatletter
\newtcolorbox{halfframedbox}[2]{
  empty,
  coltitle = #1,
  title = #2,
  overlay = {
    \draw[gray!60,line width=1pt](frame.north west)--(frame.north east)--(frame.south east)--(frame.south west)--(frame.north west);
    \draw[#1,line width=1pt]
    ($(frame.north west)!0.33!(frame.south west)$)
    --(frame.north west)
    --($(frame.north west)!0.33!(frame.north east)$);
    \draw[#1,line width=1pt]
    ($(frame.south east)!0.33!(frame.south west)$)
    --(frame.south east)
    --($(frame.south east)!0.33!(frame.north east)$);
  },
}
\makeatother

\makeatletter
\newtcolorbox{boxwithfold}[2]{
  enhanced,
  before skip balanced=2mm,after skip balanced=3mm,
  boxrule=0.4pt,left=5mm,right=2mm,top=1mm,bottom=1mm,
  colback= #1,
  colframe = #2,
  sharp corners,rounded corners=southeast,arc is angular,arc=3mm,
  underlay = {%
    % Folded paper effect
    \path[fill=tcbcolback!80!black] ([yshift=3mm]interior.south east)--++(-0.4,-0.1)--++(0.1,-0.2);
    \path[draw=tcbcolframe,shorten <=-0.05mm,shorten >=-0.05mm] ([yshift=3mm]interior.south east)--++(-0.4,-0.1)--++(0.1,-0.2);
    % Symbol
    \path[fill=tcbcolback!50!black,draw=none] (interior.south west) rectangle node[white]{\Huge\bfseries !} ([xshift=4mm]interior.north west);
  },
  drop fuzzy shadow,
}
\makeatother

\makeatletter
\newtcolorbox{simpletbox}[2]{
  enhanced,
  sharp corners,
  colback = white,
  before skip = 0.2cm,
  after skip = 0.5cm,
  drop fuzzy shadow,
  colback = #1,
  colframe = #2,
  boxrule = 0pt,
  leftrule = 6pt,
}
\makeatother

\makeatletter
\newtcolorbox{generalbox}[2][]{
  enhanced,
  breakable,
  colback=white,
  colframe=azure-gradient-2!80!black,
  drop fuzzy shadow,
  attach boxed title to top left={yshift*=-\tcboxedtitleheight},
  fonttitle=\bfseries,
  title={\optional{#2}},
  boxed title size=title,
  boxed title style={
    sharp corners,
    rounded corners=northwest,
    colback=tcbcolframe,
    boxrule=0pt,
  },
  underlay boxed title={%
    \path[fill=tcbcolframe] (title.south west)--(title.south east)
    to[out=0, in=180] ([xshift=5mm]title.east)--
    (title.center-|frame.east)
    [rounded corners=\kvtcb@arc] |-
    (frame.north) -| cycle;
  },
  #1
}
\makeatother

\theoremstyle{plain}
\newtheorem*{theorem*}{Teorema}
\newtheorem*{definition*}{Definizione}
\newtheorem*{lemma*}{Lemma}
\newtheorem*{proposition*}{Proposizione}
\newtheorem*{corollary*}{Corollario}
\newtheorem*{axiom*}{Assioma}
\newtheorem{definition}{Definizione}
\newtheorem{theorem}{Teorema}
\newtheorem{lemma}{Lemma}
\newtheorem{proposition}{Proposizione}
\newtheorem{corollary}{Corollario}
\newtheorem{axiom}{Assioma}

% Custom environments
\newcounter{exmp}[section]
\renewcommand{\theexmp}{\thesection.\arabic{exmp}}
\newenvironment{example}[1][]{%
	\refstepcounter{exmp}%
	\begin{halfframedbox}{red}%
		{\textbf{Esempio~\theexmp} \ifblank{#1}{}{: #1} \rmfamily}}%
	{\end{halfframedbox}}

\newcounter{teorcounter}[section]
\renewcommand{\theteorcounter}{\thesection.\arabic{teorcounter}}
\newenvironment{teorbox}[1][]{\refstepcounter{teorcounter}\begin{generalbox}[colframe=blue!75!black]{Teorema~\theteorcounter~\ifblank{#1}{}{(#1)}}}{\end{generalbox}}

\newcounter{exscounter}[section]
\renewcommand{\theexscounter}{\thesection.\arabic{exscounter}}
\newenvironment{exsbox}{\refstepcounter{exscounter}%
\begin{generalbox}[colframe=blue]{Esercizio~\theexscounter}}%
{\end{generalbox}}

\newcounter{corolcounter}[section]
\renewcommand{\thecorolcounter}{\thesection.\arabic{corolcounter}}
\newenvironment{corolbox}[1][]{\refstepcounter{corolcounter}\begin{generalbox}[colframe=orange!95!black]{Corollario~\thecorolcounter~\ifblank{#1}{}{(#1)}}}{\end{generalbox}}

\newcounter{propcounter}[section]
\renewcommand{\thepropcounter}{\thesection.\arabic{propcounter}}
\newenvironment{propbox}[1][]
{
	\refstepcounter{propcounter}
	\begin{generalbox}[colframe=blue!75]{Proposizione~\thepropcounter~\ifblank{#1}{}{(#1)}}
}
	{
		\end{generalbox}
	}

\newcounter{lemmacounter}[section]
\renewcommand{\thelemmacounter}{\thesection.\arabic{lemmacounter}}
\newenvironment{lemmabox}[1][]{\refstepcounter{lemmacounter}\begin{generalbox}[colframe=pink!75]{Lemma~\thelemmacounter~\ifblank{#1}{}{(#1)}}}{\end{generalbox}}

\newcounter{axiomcounter}[section]
\renewcommand{\theaxiomcounter}{\thesection.\arabic{axiomcounter}}
\newenvironment{axiombox}[1]{\refstepcounter{axiomcounter}\begin{generalbox}[colframe=green!75!black]{Assioma~\theaxiomcounter~\ifblank{#1}{}{#1}}}{\end{generalbox}}

\newcounter{definitcounter}[section]
\renewcommand{\thedefinitcounter}{\thesection.\arabic{definitcounter}}
\newenvironment{defbox}[1]{
	\refstepcounter{definitcounter}\begin{Definition*}[colbacktitle=red!75!black]{Definizione~\thedefinitcounter: #1}}
	{\end{Definition*}}
\newcommand{\dfn}[2]{\refstepcounter{definitcounter}\begin{Definition*}[colbacktitle=red!75!black]{Definizione~\thedefinitcounter: #1}{}#2\end{Definition*}} % Invoke with \dfn{Title}{CONTENT}

\newcommand{\info}[2]{\begin{infobox}{#1}#2\end{infobox}} % Invoke with \info{title}{Content}
\newcommand{\warning}[2]{\begin{warningbox}{#1}#2\end{warningbox}} % Invoke with \warning{title}{Content}
\newcommand{\error}[2]{\begin{errorbox}{#1}#2\end{errorbox}} % Invoke with \error{title}{Content}
\newcommand{\infoalt}[2]{\begin{infoboxtag}{#1}#2\end{infoboxtag}} % Invoke with \infoalt{icon_name}{Content}
\newcommand{\framedbox}[3]{\begin{halfframedbox}{#1}{#2}#3\end{halfframedbox}} % Invoke with \framedbox{color}{Title}{content}
\newcommand{\marker}[3]{\begin{boxwithfold}{#1}{#2}#3\end{boxwithfold}} % Invoke with \marker{color}{border-color}{content}
\newcommand{\simplebox}[3]{\begin{simpletbox}{#1}{#2}#3\end{simpletbox}} % Invoke with \simplebox{color}{border-color}{content}
\newcommand{\gbox}[3]{\begin{generalbox}[colframe=#2]{#1}#3\end{generalbox}} % Invoke with \gbox{[NEEDED] TITLE}{[NEEDED] FRAME COLOR}{[NEEDED] CONTENT}
\newcommand{\tbox}[2]{\begin{teorbox}{#1}{}#2\end{teorbox}}
\newcommand{\cbox}[2]{\begin{corolbox}{#1}{}#2\end{corolbox}}
\newcommand{\pbox}[2]{\begin{propbox}{#1}{}#2\end{propbox}}
\newcommand{\lbox}[2]{\begin{lemmabox}{#1}{}#2\end{lemmabox}}
\newcommand{\ebox}[2]{\begin{example}{#1}{}#2\end{example}}
\newcommand{\abox}[2]{\begin{axiombox}{#1}{}#2\end{axiombox}}

\newcounter{oss}[section]
\newenvironment{osservation}[1][]
{
	\refstepcounter{oss}\begin{infobox}{Osservazione~\theoss}\ifblank{#1}{}{#1}
}
{
\end{infobox}
}
\renewcommand{\theoss}{\thesection.\arabic{oss}}
\newcommand{\oss}[1][]{\refstepcounter{oss}\begin{infobox}{Osservazione~\theoss}\ifblank{#1}{}{#1}\end{infobox}}
% Code highlighting  w/ LstListing
\makeatletter
        \lst@Key{firstlineandnumber}\relax{\def\lst@firstline{#1\relax}\def\lst@firstnumber{#1\relax}}
\makeatother

\makeatletter\renewcommand*\verbatim@nolig@list{}\makeatother

% \lstdefinestyle{UninaDocsStyle}{
%   basewidth=0.6em,
%   literate=
%     {www}{{www}}3
%     {**}{{**}}2
%     {***}{{***}}3
%     {*>}{{*>}}2
%     {::}{{::}}2
%     {:::}{{:::}}3
%     {:=}{{:=}}2
%     {!!}{{!!}}2
%     {!=}{{!=}}2
%     {!==}{{!==}}3
%     % {--}{{--}}2 -- Interferes with comment highlighting
%     {---}{{---}}3
%     {-->}{{-->}}3
%     {->}{{->}}2
%     {->>}{{->>}}3
%     {-<}{{-<}}2
%     {-<<}{{-<<}}3
%     {-~}{{-\textasciitilde}}2
%     {\#\{}{{\#\{}}2
%     {\#[}{{\#[}}2
%     {\#\#}{{\#\#}}2
%     {\#\#\#}{{\#\#\#}}3
%     {\#\#\#\#}{{\#\#\#\#}}4
%     {\#(}{{\#(}}2
%     {\#?}{{\#?}}2
%     {\#_}{{\#\_}}2
%     {\#_(}{{\#\_(}}3
%     {.-}{{.-}}2
%     {.=}{{.=}}2
%     {..}{{..}}2
%     {..<}{{..<}}3
%     {...}{{...}}3
%     {?=}{{?=}}2
%     {??}{{??}}2
%     {/=}{{/=}}2
%     {/==}{{/==}}3
%     {/>}{{/>}}2
%     {\&\&}{{\&\&}}2
%     {||}{{||}}2
%     {||=}{{||=}}3
%     {|=}{{|=}}2
%     {|>}{{|>}}2
%     {^=}{{\textasciicircum=}}2
%     {$>}{{\$>}}2
%     {++}{{++}}2
%     {+>}{{+>}}2
%     {=:=}{{=:=}}3
%     {==}{{==}}2
%     {===}{{===}}3
%     {==>}{{==>}}3
%     {=>}{{=>}}2
%     {=>>}{{=>>}}3
%     {=<<}{{=<<}}3
%     {=/=}{{=/=}}3
%     {>-}{{>-}}2
%     {>=}{{>=}}2
%     {>=>}{{>=>}}3
%     {>>}{{>>}}2
%     {>>-}{{>>-}}3
%     {>>=}{{>>=}}3
%     {>>>}{{>>>}}3
%     {<*}{{<*}}2
%     {<*>}{{<*>}}3
%     {<|}{{<|}}2
%     {<|>}{{<|>}}3
%     {<$}{{<\$}}2
%     {<$>}{{<\$>}}3
%     {<!--}{{<!--}}4
%     {<-}{{<-}}2
%     {<--}{{<--}}3
%     {<->}{{<->}}3
%     {<+}{{<+}}2
%     {<+>}{{<+>}}3
%     {<=}{{<=}}2
%     {<==}{{<==}}3
%     {<=>}{{<=>}}3
%     {<=<}{{<=<}}3
%     {<>}{{<>}}2
%     {<<}{{<<}}2
%     {<<-}{{<<-}}3
%     {<<=}{{<<=}}3
%     {<<<}{{<<<}}3
%     {<~}{{<\textasciitilde}}2
%     {<~~}{{<\textasciitilde\textasciitilde}}3
%     {</}{{</}}2
%     {~@}{{\textasciitilde @}}2
%     {~-}{{\textasciitilde-}}2
%     {~=}{{\textasciitilde=}}2
%     {~>}{{\textasciitilde>}}2
%     {~~}{{\textasciitilde\textasciitilde}}2
%     {~~>}{{\textasciitilde\textasciitilde>}}3
%     {\%\%}{{\%\%}}2
%     {<==>}{{<==>}}4
%     {<||}{{<||}}3
%     {<|||}{{<|||}}4
%     {|||>}{{|||>}}4
%     {</>}{{</>}}3
%     {<~>}{{<\textasciitilde>}}3
%     {<-<}{{<-<}}3
%     {>->}{{>->}}3
%     {=!=}{{=!=}}3
%     {+++}{{+++}}3
%     {||>}{{||>}}3
%     {_|_}{{\_|\_}}3
%     {..=}{{..=}}3
%     {!!.}{{!!.}}3
%     {::=}{{::=}}3
%     {<:}{{<:}}2
%     {>:}{{>:}}2
%     {|-}{{|-}}2
%     {|]}{{|]}}2
%     {|\}}{{|\}}}2
%     {__}{{\_\_}}2
%     {?.}{{?.}}2
%     {?:}{{?:}}2
%     {.?}{{.?}}2
%     {\#=}{{\#=}}2
%     {\#!}{{\#!}}2
%     {\#:}{{\#:}}2
%     {-|}{{-|}}2
%     {:<}{{:<}}2
%     {:>}{{:>}}2
%     {]\#}{{]\#}}2
%     {[|}{{[|}}2
%     {\{|}{{\{|}}2
% }


\lstset{%
	autogobble=true,
	mathescape=true,
	% style=UninaDocsStyle,
	backgroundcolor=\color{naplesyellow!10!white},
	commentstyle=\itshape\color{seagreen},
	keywordstyle=\color{cherryred},
	stringstyle=\color{naplesyellow!80!black},
	basicstyle=\linespread{1.15}\footnotesize\FiraCode,
	breaklines=true,
	tabsize=2,
	escapeinside={(*@}{@*)},
	xleftmargin=0.5cm,
	xrightmargin=0.5cm,
	framesep=0mm,
	extendedchars=true,
	showstringspaces=false,
	showtabs=false,
	showstringspaces=false,
	captionpos=b,
	title=\color{primary}\fontsize{10}{12}\selectfont\sp@rtext{\lstname}{\ifilecode},
	columns=fixed,
	}

\lstdefinestyle{wnumbers}{
  % Style of numbers
  xleftmargin=1cm,
  numbers=left,
  numberstyle=\Lato\scriptsize\color{orange!80!black},
  frame=l,
  framesep=0.5cm,
  framexleftmargin=0.3cm,
  rulecolor=\color{white},
  fillcolor=\color{naplesyellow!40!white}
}

\lstdefinelanguage{makefile}{%
  morecomment=[l][commentstyle]{\#},%
}

\lstdefinelanguage{ini}{
  xleftmargin=1cm,
  numbers=left,
  numberstyle=\Lato\scriptsize\color{orange!80!black},
  frame=l,
  framesep=0.5cm,
  framexleftmargin=0.3cm,
  morecomment=[s][\color{violet!60!white}\bfseries]{[}{]},
  morecomment=[l]{\#},
  morecomment=[l]{;},
  commentstyle=\color{gray}\ttfamily,
  morekeywords={},
  otherkeywords={=,:},
  keywordstyle={\color{turquoise!70!black}\bfseries},
  % Automatically skip leading spaces
  autogobble=false,
  escapeinside={(*}{*)}
}

\lstdefinelanguage{asd}[]{}{
  alsoletter={\#},
  sensitive=true,
  xleftmargin=1cm,
  numbers=left,
  numberstyle=\Lato\scriptsize\color{orange!80!black},
  frame=l,
  framesep=0.5cm,
  framexleftmargin=0.3cm,
  %% Handle directives
  %% moredelim=*[s][{\color{violet!60!white}}]{\#include\ <}{.h>},
  %% moredelim=*[l][{\color{blue!65!white}}]\#if,
  %% moredelim=*[l][{\color{blue!65!white}}]\#endif,
  %% moredelim=*[s][{\color{violet!60!white!70!black}}]{\#include\ "}{.h"},
  %% moredelim=*[l][{\color{blue!90!white!70!black}}]\#define,
  moredelim=*[s][{\color{turquoise!70!black}}]{[}{]},
  %% Comments
  comment=[l]//,
  morecomment=[s]{/*}{*/},
  % Strings
  string=[b]{"},
  morestring=[b]{'},
  % Modifiers
  keywords=[0]{i, j, k, n},
  % Primitives
  keywords=[1]{if, else, then, for, while, do},
  % Logical constructs
  keywords=[2]{return},
  keywords=[3]{SUM, MAX, RIS, A, Search},
  keywords=[4]{free, pos},
  % Operators
  otherkeywords={*, &},
  keywords=[3]{*, &},
  escapechar={@},
  % API structs
  % Colors
  directivestyle=\color{blue!85!white},
  keywordstyle=[0]\color{cherryred},
  keywordstyle=[1]\color{green!75!black},
  keywordstyle=[2]\color{cherryred!75},
  keywordstyle=[3]\color{naplesyellow!75!black},
  keywordstyle=[4]\color{violet!75!black},
}[keywords,comments,strings,directives]

\lstdefinelanguage{usql}{
  language = SQL,
  morekeywords = {PACKAGE, TYPE, REPLACE, NEW, IF, FOR, EACH,
  ROW, AFTER, LOOP, IS, RETURN, PROCEDURE, FUNCTION, CURSOR, INSTR, LENGTH,
  CONCAT, TO_NUMBER, WHILE, BULK, COLLECT, MULTISET, PIPE, PIPELINED, DECLARE, RAISE, NOTICE, DO, LANGUAGE, EXIT, CONTINUE, RETURNS, REFCURSOR, OPEN, FETCH, MOVE, NEXT, LAST, PRIOR, FIRST, ABSOLUTE,  CLOSE, INFO, LOG, DEBUG, WARNING, BEFORE, STATEMENT, GENERATED, ALWAYS, RENAME, STRICT, SETOF, TEXT, TRANSACTION, WORK, SYS_REFCURSOR
	},
  morecomment=[l]{--},
	% Strings
	string=[b]{"},
	morestring=[b]{'},
}[keywords,comments,strings,directives]

\lstdefinelanguage[POSIX]{JAVA}
                  []{}
                  { alsoletter={\#},
                    sensitive=true,
                    %% Handle directives
                    moredelim=*[s][{\color{violet!60!white}}]package,
                    moredelim=*[l][{\color{blue!65!white}}]import,
                    %% Comments
                    comment=[l]//,
                    morecomment=[s]{/*}{*/},
                    % Strings
                    string=[b]{"},
                    morestring=[b]{'},
                    % Modifiers
                    keywords=[0]{class, public, private, static, volatile, Enum},
                    % Primitives
                    keywords=[1]{int, Integer, String, double, long, short, char, void, bool},
                    % Logical constructs
                    keywords=[2]{if, else, return, for, while, do},
                    % Operators
                    otherkeywords={*, &},
                    keywords=[3]{*, &},
                    % Colors
                    directivestyle=\color{blue!85!white},
                    keywordstyle=[0]\color{cherryred},
                    keywordstyle=[1]\color{green!75!black},
                    keywordstyle=[2]\color{cherryred},
                    keywordstyle=[3]\color{naplesyellow!75!black},
                  }[keywords,comments,strings,directives]

\lstdefinelanguage[POSIX]{C}
                  []{}
                  { alsoletter={\#},
                    sensitive=true,
                    %% Handle directives
                    moredelim=*[s][{\color{violet!60!white}}]{\#include\ <}{.h>},
                    moredelim=*[l][{\color{blue!65!white}}]\#if,
                    moredelim=*[l][{\color{blue!65!white}}]\#endif,
                    moredelim=*[s][{\color{violet!60!white!70!black}}]{\#include\ "}{.h"},
                    moredelim=*[l][{\color{blue!90!white!70!black}}]\#define,
                    moredelim=*[s][{\color{turquoise!70!black}}]{[}{]},
                    %% Comments
                    comment=[l]//,
                    morecomment=[s]{/*}{*/},
                    % Strings
                    string=[b]{"},
                    morestring=[b]{'},
                    % Modifiers
                    keywords=[0]{const, unsigned, signed, static, volatile, typedef, struct, union, enum, atomic_t},
                    % Primitives
                    keywords=[1]{int, float, double, long, short, char, void, bool},
                    % Logical constructs
                    keywords=[2]{if, else, return, for, while, do},
                    % Operators
                    otherkeywords={*, &},
                    keywords=[3]{*, &},
                    % API structs
                    % Colors
                    directivestyle=\color{blue!85!white},
                    keywordstyle=[0]\color{cherryred},
                    keywordstyle=[1]\color{green!75!black},
                    keywordstyle=[2]\color{cherryred},
                    keywordstyle=[3]\color{naplesyellow!75!black},
                  }[keywords,comments,strings,directives]

% Define UNINA style for Bash
\lstdefinelanguage[UNINA]{bash}[]{}{
	language=bash,
	sensitive=true,
	literate = {\$\#}{{{\$\#}}}2,
	morekeywords={%
		if, then, else, elif, fi, for, while, do, done,
		case, esac, function, return, in, break, continue,
		select, until, shift, exit, eval, exec%
	},%
	morekeywords=[2]{%
		alias, bind, builtin, caller, command, declare, echo,
		enable, help, let, local, logout, mapfile, printf,
		read, readarray, source, type, typeset, ulimit,
		unalias, set, shopt, export, unset, readonly%
	},%
	morekeywords=[3]{%
		awk, basename, cat, cd, chmod, chown, cp, cut, date,
		df, diff, dirname, du, find, grep, head, kill, ls,
		mkdir, mv, paste, ping, ps, pwd, rm, rmdir, sed,
		sleep, sort, ssh, tail, tar, touch, tr, uniq, wc,
		wget, which, xargs, shutdown%
	},%
	morecomment=[l]{\#},%
	morestring=[b]",%
	morestring=[b]',%
	morestring=[s]{`}{`},%
	keywordstyle={\color{blue}\bfseries},%
	keywordstyle=[2]{\color{cyan}\bfseries},%
	keywordstyle=[3]{\color{red}},%
	keywordstyle=[4]{\color{violet}\ttfamily},%
	keywordstyle=[5]{\color{orange}\bfseries},%
	commentstyle={\color{teal}\itshape},%
	backgroundcolor={\color{black!3}},%
}[keywords,comments,strings,directives]

\lstdefinelanguage[UNINA]{cpp}[]{}{
	language=C++,
	sensitive=true,
	moredelim = [s][\color{gray}]{/*}{*/},
}

\lstdefinelanguage[UNINA]{html}[]{}{
  language=html,
  sensitive=true,
  alsoletter={<>=-/},
  morecomment=[s]{<!--}{-->},
  tag=[s],
  otherkeywords={
    % General
    >,
    % Standard tags
    <!DOCTYPE,
    form, action=, method,
    </html, <html, <head, <title, </title, <style, </style, <link, </head, <meta, />,
    % body
    </body, <body,
    % Divs
    </div, <div, </div>, 
    % Paragraphs
    </p, <p, </p>, </h1, <h1, </h1>, </dl, <dl, </dl>, </dt, <dt, </dt>, </dd, <dd, </dd>, </ul, <ul, </ul>, </li, <li, </li>, </ol, <ol, </ol>,
    % scripts
    </script, <script, <label, </label>, <input,
    % More tags...
    <canvas, /canvas>, <svg, <rect, <animateTransform, </rect>, </svg>, <video, <source, <iframe, </iframe>, </video>, <image, </image>, <header, </header, <article, </article, <table, <caption, <tr, <th, <td, </table, </caption, </tr, </th, </td
  },
  ndkeywords={
    % General
    =,
    % HTML attributes
    charset=, src=, id=, width=, height=, style=, type=, rel=, href=, name=,
    % SVG attributes
    fill=, attributeName=, begin=, dur=, from=, to=, poster=, controls=, x=, y=, repeatCount=, xlink:href=,
    % properties
    margin:, padding:, background-image:, border:, top:, left:, position:, width:, height:, margin-top:, margin-bottom:, font-size:, line-height:,
    % CSS3 properties
    transform:, -moz-transform:, -webkit-transform:,
    animation:, -webkit-animation:,
    transition:,  transition-duration:, transition-property:, transition-timing-function:,
  }
}

\lstdefinelanguage[UNINA]{css}[]{}{
	morekeywords={accelerator,azimuth,background,background-attachment,selector, property,
	background-color,background-image,background-position,
	background-position-x,background-position-y,background-repeat,
	behavior,border,border-bottom,border-bottom-color,
	border-bottom-style,border-bottom-width,border-collapse,
	border-color,border-left,border-left-color,border-left-style,
	border-left-width,border-right,border-right-color,
	border-right-style,border-right-width,border-spacing,
	border-style,border-top,border-top-color,border-top-style,
	border-top-width,border-width,bottom,caption-side,clear,
	clip,color,content,counter-increment,counter-reset,cue,
	cue-after,cue-before,cursor,direction,display,elevation,
	empty-cells,filter,float,font,font-family,font-size,
	font-size-adjust,font-stretch,font-style,font-variant,
	font-weight,height,ime-mode,include-source,
	layer-background-color,layer-background-image,layout-flow,
	layout-grid,layout-grid-char,layout-grid-char-spacing,
	layout-grid-line,layout-grid-mode,layout-grid-type,left,
	letter-spacing,line-break,line-height,list-style,
	list-style-image,list-style-position,list-style-type,margin,
	margin-bottom,margin-left,margin-right,margin-top,
	marker-offset,marks,max-height,max-width,min-height,
	min-width,-moz-binding,-moz-border-radius,
	-moz-border-radius-topleft,-moz-border-radius-topright,
	-moz-border-radius-bottomright,-moz-border-radius-bottomleft,
	-moz-border-top-colors,-moz-border-right-colors,
	-moz-border-bottom-colors,-moz-border-left-colors,-moz-opacity,
	-moz-outline,-moz-outline-color,-moz-outline-style,
	-moz-outline-width,-moz-user-focus,-moz-user-input,
	-moz-user-modify,-moz-user-select,orphans,outline,
	outline-color,outline-style,outline-width,overflow,
	overflow-X,overflow-Y,padding,padding-bottom,padding-left,
	padding-right,padding-top,page,page-break-after,
	page-break-before,page-break-inside,pause,pause-after,
	pause-before,pitch,pitch-range,play-during,position,quotes,
	-replace,richness,right,ruby-align,ruby-overhang,
	ruby-position,-set-link-source,size,speak,speak-header,
	speak-numeral,speak-punctuation,speech-rate,stress,
	scrollbar-arrow-color,scrollbar-base-color,
	scrollbar-dark-shadow-color,scrollbar-face-color,
	scrollbar-highlight-color,scrollbar-shadow-color,
	scrollbar-3d-light-color,scrollbar-track-color,table-layout,
	text-align,text-align-last,text-decoration,text-indent,
	text-justify,text-overflow,text-shadow,text-transform,
	text-autospace,text-kashida-space,text-underline-position,top,
	unicode-bidi,-use-link-source,vertical-align,visibility,
	voice-family,volume,white-space,widows,width,word-break,
	word-spacing,word-wrap,writing-mode,z-index,zoom},
	morestring=[s]{:}{;},
	sensitive,
	morecomment=[s]{/*}{*/}
	}
\lstdefinelanguage[UNINA]{js}[]{}{
    alsoletter={.},
    keywords={arguments,await,break,case,catch,class,const,continue,debugger,default,delete,do,else,enum,eval,export,extends,false,finally,for,function,if,implements,import,in,instanceof,interface,let,new,null,package,private,protected,public,return,static,super,switch,this,throw,true,try,typeof,var,void,while,with,yield}, % JavaScript ES6 keywords
    ndkeywords={add, apply, args, Array, Array.from, Array.isArray, Array.of , Array.prototype, ArrayBuffer, bind, Boolean, call, charAt, charCodeAt, clear, codePointAt, concat, constructor, copyWithin, DataView, Date, Date.now, Date.parse, Date.prototype, Date.UTC, decodeURI, decodeURIComponent, encodeURI, encodeURIComponent, endsWith, entries, Error, Error.prototype, EvalError, every, false, fill, filter, find, findIndex, Float32Array, Float64Array, forEach, FulfillPromise, Function, Function.length, get, getDate, getDay, getFullYear, getHours, getMilliseconds, getMinutes, getMonth, getSeconds, getTime, getTimezoneOffset, getUTCDate, getUTCDay, getUTCFullYear, getUTCHours, getUTCMilliseconds, getUTCMinutes, getUTCMonth, getUTCSeconds, has,hasInstance, hasOwnProperty, ignoreCase, includes, indexOf, indexOf, Infinity, Int8Array, Int16Array, Int32Array, isConcatSpreadable, isFinite, isNaN, IsPromise, isPrototypeOf, Iterable, iterator, join, JSON, JSON.parse, JSON.stringify, keys, lastIndexOf, lastIndexOf, length, localeCompare, map, Map, match, match, Math, Math.abs , Math.acos, Math.acosh, Math.asin, Math.asinh, Math.atan, Math.atan2, Math.atanh, Math.cbrt, Math.ceil, Math.clz32, Math.cos, Math.cosh,  Math.E, Math.exp, Math.expm1, Math.floor, Math.fround, Math.hypot, Math.imul, Math.LN2, Math.LN10, Math.log, Math.log1p, Math.log2, Math.LOG2E, Math.log10, Math.LOG10E, Math.max, Math.min, Math.PI, Math.pow, Math.random, Math.round, Math.sign, Math.sin, Math.sinh, Math.sqrt, Math.SQRT2, Math.tan, Math.tanh, Math.trunc, message, multiline, name, NaN, NewPromiseCapability, next, normalize, null, Number, Number.EPSILON, Number.isFinite, Number.isInteger, Number.isNaN, Number.isSafeInteger, Number.MAX_SAFE_INTEGER, Number.MAX_VALUE, Number.MIN_SAFE_INTEGER, Number.MIN_VALUE, Number.NaN, Number.parseFloat, Number.parseInt, Number.POSITIVE_INFINITY, Number.prototype, Object, Object, Object.assign, Object.create, Object.defineProperties, Object.defineProperty, Object.freeze, Object.getOwnPropertyDescriptor, Object.getOwnPropertyNames, Object.getOwnPropertySymbols, Object.getPrototypeOf, Object.is, Object.isExtensible, Object.isFrozen, Object.isSealed, Object.keys, Object.preventExtensions, Object.prototype, Object.seal, Object.setPrototypeOf, of, parseFloat, parseInt, pop, Promise, Promise.all , Promise.race, Promise.reject, Promise.resolve, PromiseReactionJob, propertyIsEnumerable, prototype, Proxy, Proxy.revocable , push, RangeError, reduce, reduceRight, ReferenceError, Reflect, Reflect.apply, Reflect.construct , Reflect.defineProperty, Reflect.deleteProperty, Reflect.enumerate, Reflect.get, Reflect.getOwnPropertyDescriptor, Reflect.getPrototypeOf, Reflect.has, Reflect.isExtensible, Reflect.ownKeys, Reflect.preventExtensions, Reflect.set, Reflect.setPrototypeOf, Reflection, RegExp, RegExp, RegExp.prototype, repeat, replace, replace, reverse, search, search, Set, set, setDate, setFullYear, setHours, setMilliseconds, setMinutes, setMonth, setSeconds, setTime, setUTCDate, setUTCFullYear, setUTCHours, setUTCMilliseconds, setUTCMinutes, setUTCMonth, setUTCSeconds, shift, slice, slice, some, sort, species, splice, split, split, startsWith, String, String.fromCharCode, String.fromCodePoint, String.raw, substring, Symbol, Symbol.for, Symbol.hasInstance, Symbol.isConcatSpreadable, Symbol.iterator, Symbol.keyFor, Symbol.match, Symbol.prototype, Symbol.replace, Symbol.replace, Symbol.search, Symbol.species, Symbol.split, Symbol.toPrimitive, Symbol.toStringTag, Symbol.unscopables, SyntaxError, then, toDateString, toExponential, toFixed, toISOString, toJSON, toLocaleDateString, toLocaleLowerCase, toLocaleString, toLocaleString, toLocaleString, toLocaleString, toLocaleTimeString, toLocaleUpperCase, toLowerCase, toPrecision, toPrimitive, toString, toStringTag, toTimeString, toUpperCase, toUTCString, TriggerPromiseReactions, trim, true, TypeError, Uint8Array, Uint8ClampedArray, Uint16Array, Uint32Array, undefined, unscopables, unshift, URIError, valueOf, WeakMap, WeakSet
    }, % JavaScript extended keywords
    ndkeywordstyle=\color{cherryred}\bfseries,
    % identifierstyle=\color{identifiers},
    sensitive=true,
    % stringstyle=\color{string}\ttfamily,
    morestring=[b]",
    morestring=[d]',
    morestring=[s]{`}{`},
    % commentstyle=\color{red}\itshape,
    morecomment=[l]{//},
    morecomment=[s]{/*}{*/},
    morecomment=[s]{/**}{*/},
    emph={app.all, app.delete, app.disable, app.disabled, app.enable, app.enabled, app.engine, app.get, app.listen, app.locals, app.METHOD, app.mountpath, app.param, app.path, app.post, app.put, app.render, app.route, app.set, app.use, express, express.Router, express.static, req.acceptLanguages, req.accepts, req.acceptsCharsets, req.acceptsEncodings, req.app, req.baseUrl, req.body, req.cookies, req.fresh, req.get, req.hostname, req.ip, req.ips, req.is, req.method, req.originalUrl, req.param, req.params, req.path, req.protocol, req.query, req.range, req.route, req.secure, req.signedCookies, req.stale, req.subdomains, req.xhr, res.app, res.append, res.attachment, res.clearCookie, res.cookies, res.download, res.end, res.format, res.get, res.headersSent, res.json, res.jsonp, res.links, res.locals, res.location, res.redirect, res.render, res.sendFile, res.sendStatus, res.set, res.status, res.type, res.vary, router.all, router.METHOD, router.param, router.route, router.use}, % express keywords
    emph={[2]agent.createConnection, agent.destroy, agent.freeSockets, agent.getName, agent.maxFreeSockets, agent.maxSockets, agent.requests, agent.sockets, certificate.exportChallenge, certificate.exportPublicKey, certificate.verifySpkac, child.channel, child.connected, child.disconnect, child.kill, child.pid, child.send, child.stderr, child.stdin, child.stdio, child.stdout, child_process.exec, child_process.execFile, child_process.execFileSync, child_process.execSync, child_process.fork, child_process.spawn, child_process.spawnSync, cipher.final, cipher.getAuthTag, cipher.setAAD, cipher.setAutoPadding, cipher.update, clearImmediate, clearImmediate, clearInterval, clearInterval, clearTimeout, clearTimeout, console, console.assert, console.dir, console.error, console.info, console.log, console.time, console.timeEnd, console.trace, console.warn, decipher.final, decipher.setAAD, decipher.setAuthTag, decipher.setAutoPadding, decipher.update, dgram.createSocket, dgram.createSocket, diffieHellman.computeSecret, diffieHellman.generateKeys, diffieHellman.getGenerator, diffieHellman.getPrime, diffieHellman.getPrivateKey, diffieHellman.getPublicKey, diffieHellman.setPrivateKey, diffieHellman.setPublicKey, diffieHellman.verifyError, dns.getServers, dns.getServers, dns.lookup, dns.lookup, dns.lookupService, dns.resolve, dns.resolve4, dns.resolve6, dns.resolveCname, dns.resolveMx, dns.resolveNaptr, dns.resolveNs, dns.resolvePtr, dns.resolveSoa, dns.resolveSrv, dns.resolveTxt, dns.reverse, dns.setServers, ecdh.computeSecret, ecdh.generateKeys, ecdh.getPrivateKey, ecdh.getPublicKey, ecdh.setPrivateKey, ecdh.setPublicKey, error.address, error.code, error.errno, error.message, error.path, error.port, error.stack, error.syscall, exports, fs.access, fs.accessSync, fs.appendFile, fs.appendFileSync, fs.chmod, fs.chmodSync, fs.chown, fs.chownSync, fs.close, fs.closeSync, fs.constants, fs.createReadStream, fs.createWriteStream, fs.exists, global, http.createServer, http.get, http.globalAgent, http.request, https.createServer, https.get, https.globalAgent, https.request, message.destroy, message.headers, message.httpVersion, message.method, message.rawHeaders, message.rawTrailers, message.setTimeout, message.socket, message.statusCode, message.statusMessage, message.trailers, message.url, module, module.children, module.exports, module.filename, module.id, module.loaded, module.parent, module.require, os.arch, os.constants, os.cpus, os.endianness, os.EOL, os.freemem, os.homedir, os.hostname, os.loadavg, os.networkInterfaces, os.platform, os.release, os.tmpdir, os.totalmem, os.type, os.uptime, os.userInfo, path.basename, path.delimiter, path.dirname, path.extname, path.format, path.isAbsolute, path.join, path.normalize, path.parse, path.posix, path.relative, path.resolve, path.sep, path.win32, process, process.abort, process.arch, process.argv, process.argv0, process.channel, process.chdir, process.config, process.connected, process.cpuUsage, process.cwd, process.disconnect, process.emitWarning, process.env, process.execArgv, process.execPath, process.exit, process.exitCode, process.getegid, process.geteuid, process.getgid, process.getgroups, process.getuid, process.hrtime, process.initgroups, process.kill, process.mainModule, process.memoryUsage, process.nextTick, process.pid, process.platform, process.release, process.send, process.setegid, process.seteuid, process.setgid, process.setgroups, process.setuid, process.stderr, process.stdin, process.stdout, process.title, process.umask, process.uptime, process.version, process.versions, querystring.escape, querystring.parse, querystring.stringify, querystring.unescape, r.clearLine, readable.pause, readable.pipe, readable.push, readable.push, readable.read, readable.read, readable.resume, readable.setEncoding, readable.unpipe, readable.unshift, readable.wrap, readable._read, readStream.bytesRead, readStream.isRaw, readStream.path, readStream.setRawMode, repl.start, request.abort, request.aborted, request.end, request.flushHeaders, request.setNoDelay, request.setSocketKeepAlive, request.setTimeout, request.write, require, require.cache, require.extensions, response.addTrailers, response.end, response.finished, response.getHeader, response.getHeaderNames, response.getHeaders, response.hasHeader, response.headersSent, response.removeHeader, response.sendDate, response.setHeader, response.setTimeout, response.statusCode, response.statusMessage, response.write, response.writeContinue, response.writeHead, rl.clearScreenDown, rl.close, rl.createInterface, rl.cursorTo, rl.emitKeypressEvents, rl.moveCursor, rl.pause, rl.prompt, rl.question, rl.resume, rl.setPrompt, rl.write, script.runInNewContext, script.runInThisContext, server.addContext, server.address, server.address, server.close, server.close, server.connections, server.getTicketKeys, server.listen, server.listen, server.setTicketKeys, server.setTimeout, server.setTimeout, server.timeout, server.timeout, setImmediate, setInterval, setTimeout, socket.addMembership, socket.address, socket.bind, socket.bind, socket.close, socket.dropMembership, socket.ref, socket.send, socket.setBroadcast, socket.setMulticastLoopback, socket.setMulticastTTL, socket.setTTL, socket.unref, stream.Readable, stringDecoder.end, stringDecoder.write, timeout.ref, timeout.unref, tls.connect, tls.createSecureContext, tls.createServer, tls.getCiphers, tlsSocket.address, tlsSocket.authorizationError, tlsSocket.authorized, tlsSocket.encrypted, tlsSocket.getCipher, tlsSocket.getEphemeralKeyInfo, tlsSocket.getPeerCertificate, tlsSocket.getProtocol, tlsSocket.getSession, tlsSocket.getTLSTicket, tlsSocket.localAddress, tlsSocket.localPort, tlsSocket.remoteAddress, tlsSocket.remoteFamily, tlsSocket.remotePort, tlsSocket.renegotiate, tlsSocket.setMaxSendFragment, transform._flush, transform._transform, util.debuglog, util.deprecate, util.format, util.inherits, util.inspect, v8.getHeapStatistics, v8.setFlagsFromString, vm.createContext, vm.isContext, vm.runInContext, vm.runInDebugContext, vm.runInNewContext, vm.runInThisContext, watcher.close, worker.disconnect, worker.exitedAfterDisconnect, worker.id, worker.isConnected, worker.isDead, worker.kill, worker.process, worker.send, worker.suicide, writable.cork, writable.end, writable.setDefaultEncoding, writable.write, writeStream.bytesWritten, writeStream.columns, writeStream.path, writeStream.rows, zlib, zlib.createGunzip, zlib.createGzip, zlib.createInflate, zlib.createInflateRaw, zlib.createUnzip, zlib.deflate, zlib.deflateRaw, zlib.deflateRawSync, zlib.deflateSync, zlib.gunzip, zlib.gunzipSync, zlib.gzip, zlib.gzipSync, zlib.inflate, zlib.inflateRaw, zlib.inflateRawSync, zlib.inflateSync, zlib.unzip, zlib.unzipSync, __dirname, __filename}, % Node.js keywords
    emph={[3] assert, assert.deepEqual, assert.deepStrictEqual, assert.doesNotThrow, assert.equal, assert.fail, assert.ifError, assert.notDeepEqual, assert.notDeepStrictEqual, assert.notEqual, assert.notStrictEqual, assert.ok, assert.strictEqual, assert.throws, describe, toBe, it, xdescribe, beforeEach, afterEach, beforeAll, afterAll, expect, it, xit, xdiscribe, pending, and.callThrough, and.returnValue, and.returnValues, and.callFake, and.throwError, and.stub, .not, .calls.any, .calls.count, .calls.argsFor, .calls.allArgs, .calls.all, .calls.mostRecent, .calls.first, .calls.reset, jasmine.createSpy, jasmine.createSpyObj, jasmine.any, jasmine.anything, jasmine.objectContaining, jasmine.arrayContaining, jasmine.stringMatching, asymmetricMatch,  jasmine.clock, .not.toBeTruthy, .toBeTruthy, .not.toBeFalsy, .toBeFalsy, .not.toBeDefined .toBeDefined, .not.toBeNull .toBeNull, .not.toEqual .toEqual, .not.toBeCloseTo .toBeCloseTo, .not.toContain, .toContain, .not.toMatch, .toMatch, .not.toBeGreaterThan, .toBeGreaterThan, .not.toBeLessThan, .toBeLessThan, .toThrow, .not.toThrow, .toBeNull, .not.toBeNull, .toBeDefined, .not.toBeDefined}, % Node.js Assert, Jasmine, ... keywords
}
%% \directlua{
%%   function be_quiet ()
%%   end
%%   luatexbase.add_to_callback('start_run', be_quiet, 'stop start run')
%%   luatexbase.add_to_callback('stop_run', be_quiet, 'stop stop run')
%%   luatexbase.add_to_callback('start_page_number', be_quiet, 'stop start page')
%%   luatexbase.add_to_callback('stop_page_number', be_quiet, 'stop stop page')
%%   luatexbase.add_to_callback('start_file', be_quiet, 'stop start file')
%%   luatexbase.add_to_callback('stop_file', be_quiet, 'stop stop file')
%%   luatexbase.add_to_callback('show_warning_message', be_quiet, 'stop show warning message')
%% }

% Lstlisting code vertical guideline support
\begin{luacode}
  local gatherBuffer
  function read(line)
    texio.write_nl(line)
    table.insert(gatherBuffer, line)

    if line:match('.*stop_rec().*') then
      return line
    else return '' end
  end

  function start_rec()
    gatherBuffer  = { }
    luatexbase.add_to_callback('process_input_buffer', read, 'recording')
  end

  function stop_rec()
    luatexbase.remove_from_callback('process_input_buffer', 'recording')
  end

  function retrieve_recording()
  for ix, line in ipairs(gatherBuffer) do
      if ix ~= #gatherBuffer then
        texio.write_nl(line)
        tex.print(line)
      end
    end
  end

  function prepare()
    for ix=2, #gatherBuffer-2 do
      local match    = gatherBuffer[ix]:match('^%s+')
      local spaces  = (match and #match - 1) or 0
      texio.write_nl(tostring(spaces))

      gatherBuffer[ix] = gatherBuffer[ix]:gsub('^%s+',
      string.rep('$\\indentrule$  ', spaces))
    end
  end
\end{luacode}

% Vertical guideline
\newcommand{\indentrule}{\color{gray}\rlap{\smash{\hspace{6pt}\rule[-.35em]{1pt}{1.45em}}}}

% Lstlisting TOC & label customization
\renewcommand\lstlistlistingname{\documentlsttitle}
\renewcommand\lstlistingname{\documentlstlabel}
\makeatletter
\def\l@lstlisting#1#2{
  \@dottedtocline{1}{0em}{2.3em}{\textcolor{primary}{\ilabel \selectfont\AlteG \documentlstlabel\space #1}}{#2}
}
\makeatother

% Hypersetup configuration
\hypersetup{colorlinks, linkcolor=primary}
% Tabularray caption setup
\DefTblrTemplate{contfoot-text}{default}{(Continua nella pagina successiva)}
\DefTblrTemplate{conthead-text}{default}{}

\DefTblrTemplate{head}{default}{ }

\DefTblrTemplate{firstfoot}{default}{
  \UseTblrTemplate{contfoot}{default}
  \UseTblrTemplate{caption}{default}
}

\DefTblrTemplate{middlefoot}{default}{
  \UseTblrTemplate{contfoot}{default}
  \UseTblrTemplate{capcont}{default}
}
\DefTblrTemplate{lastfoot}{default}{
  \UseTblrTemplate{note}{default}
  \UseTblrTemplate{remark}{default}
  \UseTblrTemplate{capcont}{default}
}

% Used to output definition of a LaTeX macro in console
\def\debugdef#1{%
  \typeout{[DEBUG] Definition of \@backslashchar #1: \expandafter\meaning\csname#1\endcsname ^^J ^^J}}

% Math related
\DeclarePairedDelimiter\abs{\lvert}{\rvert}%
\DeclarePairedDelimiter\norm{\lVert}{\rVert}%

% Swap the definition of \abs* and \norm*, so that \abs
% and \norm resizes the size of the brackets, and the
% starred version does not.
\makeatletter
	\let\oldabs\abs
	\def\abs{\@ifstar{\oldabs}{\oldabs*}}
	%
	\let\oldnorm\norm
	\def\norm{\@ifstar{\oldnorm}{\oldnorm*}}

	%% Angle brackets shortcut -> <CONTENT>
	\def\abrakets#1{\displaystyle{\left\langle{#1}\right\rangle}}

	%% Curly brackets shortcut -> {CONTENT}
	\def\cbrakets#1{\displaystyle{\left\{{#1}\right\}}}
	%% Round brackets shortcut -> (CONTENT)
	\def\rbrakets#1{\displaystyle{\left({#1}\right)}}

	%% Math symbols shortcut (Calligraphic fonts with \mathcal)
	\newcommand{\mcA}{\mathcal{A}}
	\newcommand{\mcB}{\mathcal{B}}
	\newcommand{\mcC}{\mathcal{C}}
	\newcommand{\mcD}{\mathcal{D}}
	\newcommand{\mcE}{\mathcal{E}}
	\newcommand{\mcF}{\mathcal{F}}
	\newcommand{\mcG}{\mathcal{G}}
	\newcommand{\mcH}{\mathcal{H}}
	\newcommand{\mcI}{\mathcal{I}}
	\newcommand{\mcL}{\mathcal{L}}
	\newcommand{\mcM}{\mathcal{M}}
	\newcommand{\mcN}{\mathcal{N}}
	\newcommand{\mcO}{\mathcal{O}}
	\newcommand{\mcP}{\mathcal{P}}
	\newcommand{\mcQ}{\mathcal{Q}}
	\newcommand{\mcR}{\mathcal{R}}
	\newcommand{\mcS}{\mathcal{S}}
	\newcommand{\mcT}{\mathcal{T}}
	\newcommand{\mcU}{\mathcal{U}}
	\newcommand{\mcV}{\mathcal{V}}
	\newcommand{\mcW}{\mathcal{W}}
	\newcommand{\mcX}{\mathcal{X}}
	\newcommand{\mcY}{\mathcal{Y}}
	\newcommand{\mcZ}{\mathcal{Z}}

	%% Math symbols shortcut (blackboard bold fonts with \mathbb)
  \newcommand{\mbA}{\mathbb{A}}
  \newcommand{\mbB}{\mathbb{B}}
  \newcommand{\mbC}{\mathbb{C}}
  \newcommand{\mbD}{\mathbb{D}}
  \newcommand{\mbE}{\mathbb{E}}
  \newcommand{\mbF}{\mathbb{F}}
  \newcommand{\mbG}{\mathbb{G}}
  \newcommand{\mbH}{\mathbb{H}}
  \newcommand{\mbI}{\mathbb{I}}
  \newcommand{\mbJ}{\mathbb{J}}
  \newcommand{\mbK}{\mathbb{K}}
  \newcommand{\mbL}{\mathbb{L}}
  \newcommand{\mbM}{\mathbb{M}}
  \newcommand{\mbN}{\mathbb{N}}
  \newcommand{\mbO}{\mathbb{O}}
  \newcommand{\mbP}{\mathbb{P}}
  \newcommand{\mbQ}{\mathbb{Q}}
  \newcommand{\mbR}{\mathbb{R}}
  \newcommand{\mbS}{\mathbb{S}}
  \newcommand{\mbT}{\mathbb{T}}
  \newcommand{\mbU}{\mathbb{U}}
  \newcommand{\mbV}{\mathbb{V}}
  \newcommand{\mbW}{\mathbb{W}}
  \newcommand{\mbX}{\mathbb{X}}
  \newcommand{\mbY}{\mathbb{Y}}
  \newcommand{\mbZ}{\mathbb{Z}}
  \newcommand\xor{{\displaystyle{\dot{\vee}}}}

  % Symbols for outer joins
  \def\ojoin{\setbox0=\hbox{$\bowtie$}%
    \rule[-.02ex]{.25em}{.4pt}\llap{\rule[\ht0]{.25em}{.4pt}}}
  \def\leftouterjoin{\mathbin{\ojoin\mkern-5.8mu\bowtie}}
  \def\rightouterjoin{\mathbin{\bowtie\mkern-5.8mu\ojoin}}
  \def\fullouterjoin{\mathbin{\ojoin\mkern-5.8mu\bowtie\mkern-5.8mu\ojoin}}

  % Document current revision using git
  % To allow correct compilation the -shell-escape flag must be set
  \def\gitparse{\immediate\write18{git rev-parse --short HEAD > gitparse 2>&1}\input{gitparse}\immediate\write18{rm gitparse}}
  \begingroup\catcode`\%=12\relax\gdef\@percent{%}\endgroup
  \begingroup\catcode`\#=12\relax\gdef\@hash{#}\endgroup
  \def\epoch{\immediate\write18{date +'\@percentchar d \@percentchar B \@percentchar Y' > epoch}\input{epoch}\immediate\write18{rm epoch}}
\makeatother
