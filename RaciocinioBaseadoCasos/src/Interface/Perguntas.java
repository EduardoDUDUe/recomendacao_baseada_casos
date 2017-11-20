package Interface;

import Conector.Conector;
import static com.sun.media.jfxmediaimpl.MediaUtils.error;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

public class Perguntas extends javax.swing.JFrame {

    Connection conect;
    PreparedStatement pst;
    ResultSet rs;

    int crase; // variavel que pega um probabilistico de cair crase nas questões

    int regenciaVerbal; // variavel que pega um probabilistico de cair Regencia Verbal nas questões

    int regenciaNominal; // variavel que pega um probabilistico de cair Regencia Nominal nas questões

    String usuario; // Salva o usuario logado

    public Perguntas(String usuario, int crase, int regenciaVerbal, int regenciaNominal) {
        initComponents();

        conect = Conector.conetabd();

        this.crase = crase;
        this.usuario = usuario;
        this.regenciaVerbal = regenciaVerbal;
        this.regenciaNominal = regenciaNominal;

        chamarTodos();

    }

//Variavel global para pegar quantas perguntas existem e colocar no random
    int a;

    //quantidade de respostas certas do usuario
    int quantidadeQuestoes = 0;

    //Quantidade de respostas erradas do usuario
    int quantidadeRespostaErrada = 0;

    //Pega o numero da resposta certa no banco de dados e compara com a resposta do user
    int respostaCerta = 0;

    //Pega o valor da resposta do usuario
    int resposta = 0;

    //variavel para escolher as perguntas que sera utilizada no Random
    int questaoAleatoria = 0;

    //Escolhe o tipo da pergunta que vai cair. Fica entre Crase, concordancia verbal ou nominal
    int tipoPergunta = 0;

    int contadorQuestoes = 0;
    
    int erradaCrase = 0;
    
    int erradaRegenciaVerbal = 0;
    
    int erradaRegenciaNominal = 0;

    //String para pegar a pergunta no BD
    String pergunta = "";

    //String para pegar a Alternativa A no BD
    String alternativaA = "";

    //String para pegar a Alternativa B no BD
    String alternativaB = "";

    //String para pegar a Alternativa C no BD
    String alternativaC = "";

    //String para pegar a Alternativa D no BD
    String alternativaD = "";

    //String para pegar a Alternativa E no BD
    String alternativaE = "";

    public void calibrar() {
        //Aqui ajustara no banco os valores da probabilidade de cada questão cair

    }

    public void quantidadePercentualCrase() {

        String sql = "select quantidadecrase from usuario where login = ?";
        try {

            pst = conect.prepareStatement(sql);

            pst.setString(1, usuario); //Indice da busca ID na tabela crase

            rs = pst.executeQuery();
            rs.next();

            crase = rs.getInt("quantidadecrase");

        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error);
        }
    }

    public void quantidadePercentualRegenciaVerbal() {

        String sql = "select quantidaderegenciaverbal from usuario where login = ?";
        try {

            pst = conect.prepareStatement(sql);

            pst.setString(1, usuario); //Indice da busca ID na tabela Regencia Verbal

            rs = pst.executeQuery();
            rs.next();

            regenciaVerbal = rs.getInt("quantidaderegenciaverbal");

        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error);
        }
    }

    public void quantidadePercentualRegenciaNominal() {
        String sql = "select quantiregencianominal from usuario where login = ?";
        try {

            pst = conect.prepareStatement(sql);

            pst.setString(1, usuario); //Indice da busca ID na tabela Regencia Nominal

            rs = pst.executeQuery();
            rs.next();

            regenciaNominal = rs.getInt("quantiregencianominal");

        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error);
        }

    }

    public void quantidadePerguntas() {

        if (tipoPergunta <= crase) {

            String sql = "select count(*) as pergunta from crase";
            try {
                //pesquisando o valor
                pst = conect.prepareStatement(sql);

                //pegando um valor e colocando em um objeto tipo Resultset ("rs")
                rs = pst.executeQuery();
                rs.next(); //aqui foi para corrigir um erro de pegar. Foi Cleyton que disse o que fazer.

                a = rs.getInt("pergunta"); //coloca o resultado em uma variavel

                //Agora eu sei quantas perguntas existem na tebela Crase
            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
        }

        if (tipoPergunta > crase && tipoPergunta <= regenciaVerbal) {

            String sql = "select count(*) as pergunta from concordanciaverbal";
            try {
                //pesquisando o valor
                pst = conect.prepareStatement(sql);

                //pegando um valor e colocando em um objeto tipo Resultset ("rs")
                rs = pst.executeQuery();
                rs.next(); //aqui foi para corrigir um erro de pegar. Foi Cleyton que disse o que fazer.

                a = rs.getInt("pergunta"); //coloca o resultado em uma variavel

                //Agora eu sei quantas perguntas existem na tebela Crase
            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
        }

        if (tipoPergunta > regenciaVerbal && tipoPergunta <= regenciaNominal) {

            String sql = "select count(*) as pergunta from concordancianominal";
            try {

                //pesquisando o valor
                pst = conect.prepareStatement(sql);

                //pegando um valor e colocando em um objeto tipo Resultset ("rs")
                rs = pst.executeQuery();

                rs.next(); //aqui foi para corrigir um erro de pegar. Foi Cleyton que disse o que fazer.

                a = rs.getInt("pergunta"); //coloca o resultado em uma variavel

                //Agora eu sei quantas perguntas existem na tebela
            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
        }
    }

    public void respostaCerta() {

        if (tipoPergunta <= crase) {
            String sql = "select certa from crase where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                respostaCerta = rs.getInt("certa");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
        }

        if (tipoPergunta > crase && tipoPergunta <= regenciaVerbal) {

            String sql = "select certa from concordanciaverbal where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                respostaCerta = rs.getInt("certa");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
        }

        if (tipoPergunta > regenciaVerbal && tipoPergunta <= regenciaNominal) {

            // JOptionPane.showMessageDialog(null, "Nominal Resposta Certa");
            String sql = "select certa from concordancianominal where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                respostaCerta = rs.getInt("certa");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }

        }

    }

    public void Pergunta() {

        if (tipoPergunta <= crase) {
            String sql = "select pergunta from crase where id = ?";

            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                pergunta = rs.getString("pergunta");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
            tipo.setText("Crase");
        }

        if (tipoPergunta > crase && tipoPergunta <= regenciaVerbal) {

            String sql = "select pergunta from concordanciaverbal where id = ?";

            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                pergunta = rs.getString("pergunta");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
             tipo.setText("Verbal");
        }

        if (tipoPergunta > regenciaVerbal && tipoPergunta <= regenciaNominal) {

            //   JOptionPane.showMessageDialog(null, "Nominal Pergunta");
            String sql = "select pergunta from concordancianominal where id = ?";

            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                pergunta = rs.getString("pergunta");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
            
             tipo.setText("Nominal");
        }

    }

    public void RespostaA() {

        if (tipoPergunta <= crase) {
            String sql = "select respostaa from crase where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                alternativaA = rs.getString("respostaa");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
        }

        if (tipoPergunta > crase && tipoPergunta <= regenciaVerbal) {
            String sql = "select respostaa from concordanciaverbal where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                alternativaA = rs.getString("respostaa");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }

        }

        if (tipoPergunta > regenciaVerbal && tipoPergunta <= regenciaNominal) {
            String sql = "select respostaa from concordancianominal where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                alternativaA = rs.getString("respostaa");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
        }
    }

    public void RespostaB() {

        if (tipoPergunta <= crase) {
            String sql = "select respostab from crase where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                alternativaB = rs.getString("respostab");

            } catch (SQLException error) {

                JOptionPane.showMessageDialog(null, error);
            }
        }

        if (tipoPergunta > crase && tipoPergunta <= regenciaVerbal) {
            String sql = "select respostab from concordanciaverbal where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                alternativaB = rs.getString("respostab");

            } catch (SQLException error) {

                JOptionPane.showMessageDialog(null, error);
            }

        }

        if (tipoPergunta > regenciaVerbal && tipoPergunta <= regenciaNominal) {
            String sql = "select respostab from concordancianominal where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                alternativaB = rs.getString("respostab");

            } catch (SQLException error) {

                JOptionPane.showMessageDialog(null, error);
            }
        }
    }

    public void RespostaC() {

        if (tipoPergunta <= crase) {
            String sql = "select respostac from crase where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                alternativaC = rs.getString("respostac");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
        }

        if (tipoPergunta > crase && tipoPergunta <= regenciaVerbal) {

            String sql = "select respostac from concordanciaverbal where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                alternativaC = rs.getString("respostac");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
        }

        if (tipoPergunta > regenciaVerbal && tipoPergunta <= regenciaNominal) {
            String sql = "select respostac from concordancianominal where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                alternativaC = rs.getString("respostac");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
        }
    }

    public void RespostaD() {

        if (tipoPergunta <= crase) {
            String sql = "select respostad from crase where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                alternativaD = rs.getString("respostad");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
        }

        if (tipoPergunta > crase && tipoPergunta <= regenciaVerbal) {

            String sql = "select respostad from concordanciaverbal where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                alternativaD = rs.getString("respostad");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
        }

        if (tipoPergunta > regenciaVerbal && tipoPergunta <= regenciaNominal) {
            String sql = "select respostad from concordancianominal where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                alternativaD = rs.getString("respostad");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
        }
    }

    public void RespostaE() {

        if (tipoPergunta <= crase) {
            String sql = "select respostae from crase where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                alternativaE = rs.getString("respostae");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
        }

        if (tipoPergunta > crase && tipoPergunta <= regenciaVerbal) {
            String sql = "select respostae from concordanciaverbal where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                alternativaE = rs.getString("respostae");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }

        }

        if (tipoPergunta > regenciaVerbal && tipoPergunta <= regenciaNominal) {
            String sql = "select respostae from concordancianominal where id = ?";
            try {

                pst = conect.prepareStatement(sql);

                pst.setInt(1, questaoAleatoria); //Indice da busca ID na tabela crase

                rs = pst.executeQuery();
                rs.next();

                alternativaE = rs.getString("respostae");

            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, error);
            }
        }
    }

    public void colocarTela() {

        //Este metodo coloca tudo na tela para ser respondido
        TXTPergunta.setText(pergunta);
        CS1.setText(alternativaA);
        CS2.setText(alternativaB);
        CS3.setText(alternativaC);
        CS4.setText(alternativaD);
        CS5.setText(alternativaE);
    }

    public void aleatorio() {

        Random r = new Random();
        tipoPergunta = r.nextInt(150) + 1;
        // JOptionPane.showMessageDialog(null, tipoPergunta);

        quantidadePerguntas();
        // JOptionPane.showMessageDialog(null, "Numero Aleatorio");
        //Aqui vem um codigo que faz escolher a pergunta aleatoria
        Random Aleatorio = new Random();
        questaoAleatoria = Aleatorio.nextInt(a) + 1;
        //  JOptionPane.showMessageDialog(null, "Depois do Random " + questaoAleatoria);
    }

    public void chamarTodos() {
        aleatorio();
        respostaCerta();
        Pergunta();
        RespostaA();
        RespostaB();
        RespostaC();
        RespostaD();
        RespostaE();
        colocarTela();
    }

    public void valorResposta() {
        if (CS1.isSelected()) {
            resposta = 1;
        }

        if (CS2.isSelected()) {
            resposta = 2;
        }

        if (CS3.isSelected()) {
            resposta = 3;
        }

        if (CS4.isSelected()) {
            resposta = 4;
        }

        if (CS5.isSelected()) {
            resposta = 5;
        }
    }

    public void comparaResposta() {
        if (resposta == respostaCerta) {
            JOptionPane.showMessageDialog(null, "Resposta Certa");

        } else {
            JOptionPane.showMessageDialog(null, "Resposta Errada");
            quantidadeQuestoesErrada();

            if (tipoPergunta <= crase) {

                erradaCrase++;
            }

            if (tipoPergunta > crase && tipoPergunta <= regenciaVerbal) {

                erradaRegenciaVerbal++;
            }

            if (tipoPergunta > regenciaVerbal && tipoPergunta <= regenciaNominal) {

                erradaRegenciaNominal++;
            }

        }

        quantidadeQuestoes();

        contadorQuestoes += 1;

    }

    public void quantidadeQuestoesErrada() {

        String sql = "select totalerro from usuario where login = ?";
        try {

            pst = conect.prepareStatement(sql);

            pst.setString(1, usuario); //Indice da busca ID na tabela crase

            rs = pst.executeQuery();
            rs.next();

            quantidadeQuestoes = rs.getInt("totalerro");

        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error);
        }

        //Alterar quantidade total de questões.
        quantidadeQuestoes += 1;

        String sql1 = "Update usuario set totalerro = ?";

        try {

            pst = conect.prepareStatement(sql1);

            pst.setInt(1, quantidadeQuestoes);

            pst.execute();

        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error);
        }

    }

    public void quantidadeQuestoes() {

        String sql = "select totalquestoes from usuario where login = ?";
        try {

            pst = conect.prepareStatement(sql);

            pst.setString(1, usuario); //Indice da busca ID na tabela crase

            rs = pst.executeQuery();
            rs.next();

            quantidadeQuestoes = rs.getInt("totalquestoes");

        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error);
        }

        //Alterar quantidade total de questões.
        quantidadeQuestoes += 1;

        String sql1 = "Update usuario set totalquestoes = ?";

        try {

            pst = conect.prepareStatement(sql1);

            pst.setInt(1, quantidadeQuestoes);

            pst.execute();

        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error);
        }

    }

    public void CSUm() {
        CS2.setSelected(false);
        CS3.setSelected(false);
        CS4.setSelected(false);
        CS5.setSelected(false);
    }

    public void CSDois() {
        CS1.setSelected(false);
        CS3.setSelected(false);
        CS4.setSelected(false);
        CS5.setSelected(false);
    }

    public void CSTres() {
        CS2.setSelected(false);
        CS1.setSelected(false);
        CS4.setSelected(false);
        CS5.setSelected(false);
    }

    public void CSQuatro() {
        CS2.setSelected(false);
        CS3.setSelected(false);
        CS1.setSelected(false);
        CS5.setSelected(false);
    }

    public void CSCinco() {
        CS2.setSelected(false);
        CS3.setSelected(false);
        CS4.setSelected(false);
        CS1.setSelected(false);
    }

//    public void addBancoRespostaErrada() {
//        if (tipoPergunta <= crase) {
//
//            String sql = "select totalerro from usuario where login = ?";
//            try {
//
//                pst = conect.prepareStatement(sql);
//
//                pst.setString(1, usuario); //Indice da busca ID na tabela crase
//
//                rs = pst.executeQuery();
//                rs.next();
//
//                respostaErradaCrase = rs.getInt("totalerro");
//
//            } catch (SQLException error) {
//                JOptionPane.showMessageDialog(null, error);
//            }
//            
//            respostaErradaCrase += 1;
//            
//            
//            String sql1 = "Insert into usuario (nome,login,senha, quantidadecrase, quantidaderegenciaverbal, quantiregencianominal) values (?,?,?,?,?,?)";
//
//        try {
//
//            pst = conect.prepareStatement(sql1);
//
//          
//            pst.setInt(6, 150);
//
//            pst.execute();
//
//          
//
//        } catch (SQLException error) {
//            JOptionPane.showMessageDialog(null, error);
//        }
//
//            
//            
//            
//        }
//
//        if (tipoPergunta > crase && tipoPergunta <= regenciaVerbal) {
//
//            respostaErradaRegenciaVerbal += 1;
//        }
//
//        if (tipoPergunta > regenciaVerbal && tipoPergunta <= regenciaNominal) {
//
//            respostaErradaRegenciaNominal += 1;
//        }
//    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        CS1 = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        TXTPergunta = new javax.swing.JTextPane();
        CS3 = new javax.swing.JCheckBox();
        CS2 = new javax.swing.JCheckBox();
        CS4 = new javax.swing.JCheckBox();
        CS5 = new javax.swing.JCheckBox();
        tipo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        jLabel1.setText("Perguntas");

        jButton1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton1.setText("Confirmar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        CS1.setText("Alternativa A");
        CS1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CS1ActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(TXTPergunta);

        CS3.setText("Alternativa C");
        CS3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CS3ActionPerformed(evt);
            }
        });

        CS2.setText("Alternativa B");
        CS2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CS2ActionPerformed(evt);
            }
        });

        CS4.setText("Alternativa D");
        CS4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CS4ActionPerformed(evt);
            }
        });

        CS5.setText("Alternativa E");
        CS5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CS5ActionPerformed(evt);
            }
        });

        tipo.setText("Tipo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(25, 25, 25)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(CS3, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(CS4, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(CS5, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(201, 201, 201)
                                        .addComponent(tipo))
                                    .addComponent(CS1, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CS2, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 28, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(tipo))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(CS1)
                .addGap(18, 18, 18)
                .addComponent(CS2)
                .addGap(18, 18, 18)
                .addComponent(CS3)
                .addGap(18, 18, 18)
                .addComponent(CS4)
                .addGap(18, 18, 18)
                .addComponent(CS5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        valorResposta();
        comparaResposta();
        chamarTodos();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void CS4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CS4ActionPerformed
        CSQuatro();
    }//GEN-LAST:event_CS4ActionPerformed

    private void CS1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CS1ActionPerformed
        CSUm();
    }//GEN-LAST:event_CS1ActionPerformed

    private void CS2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CS2ActionPerformed
        CSDois();
    }//GEN-LAST:event_CS2ActionPerformed

    private void CS3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CS3ActionPerformed
        CSTres();
    }//GEN-LAST:event_CS3ActionPerformed

    private void CS5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CS5ActionPerformed
        CSCinco();
    }//GEN-LAST:event_CS5ActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Perguntas.class
//                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
//
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Perguntas.class
//                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
//
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Perguntas.class
//                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
//
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Perguntas.class
//                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Perguntas().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox CS1;
    private javax.swing.JCheckBox CS2;
    private javax.swing.JCheckBox CS3;
    private javax.swing.JCheckBox CS4;
    private javax.swing.JCheckBox CS5;
    private javax.swing.JTextPane TXTPergunta;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel tipo;
    // End of variables declaration//GEN-END:variables
}
