<%--
  Created by IntelliJ IDEA.
  User: ryadh
  Date: 04/11/2020
  Time: 09:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Converter</title>
</head>
<body>
    <!-- Imports  -->
    <%@page import="java.util.*" %>
    <%@page import="converter.ConverterBean" %>
    <%@ page import="javax.naming.Context" %>
    <%@ page import="javax.naming.InitialContext" %>
    <%@ page import="javax.jms.*" %>

    <!-- Injection de dependance  -->
    <jsp:useBean id="beanConv" class="converter.ConverterBean"/>

    <form action="index.jsp" method="get">

        <p>
            <label for="montant">Montant :</label>
            <input type="text" name="montant" id="montant">
        </p>

        <p>
            <label for="monnaie">Monnaie cible :</label>
            <select name="monnaie" id="monnaie">
                <option value="">--Veuillez choisir une monnaie--</option>
                <%
                    ConverterBean cb = new ConverterBean();
                    for(String s : cb.getAvailableCurrencies()){
                        String option = "<option value=\""+s+"\">"+s+"</option>";
                        out.println(option);
                    }
                %>


            </select>
        </p>

        <p>
            <label for="mail">Adresse mail :</label>
            <input type="email" name="mail" id="mail">
        </p>

        <p>
            <input type="submit" value="convertir">
        </p>

    </form>

    <!-- Affichage du resultat -->
    <%

        if (request.getParameter("montant") == null || request.getParameter("monnaie") == null){

            System.out.println("Not parameters yet");

        } else {

            String email = request.getParameter("mail");
            if(email != null && email.length() != 0){

                // Recuperer le context initial dans le serveur de noms JNDI
                Context jndiContext = new InitialContext();

                // Obtenir une instance de la fabrique de connextions
                javax.jms.ConnectionFactory connectionFactory =
                        (QueueConnectionFactory)jndiContext.lookup("/ConnectionFactory");

                // Creer une connexion
                Connection connection = connectionFactory.createConnection();

                // Creer une session sans transaction et avec des accuses de reception
                Session sessionQ =
                        connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Creer un message de type text utilisant l'objet session
                TextMessage message = sessionQ.createTextMessage();

                // Demander au MDB de déclancher la demande de conversion
                double amount  = Double.parseDouble(request.getParameter("montant"));
                String currency = request.getParameter("monnaie");
                double result = cb.euroToOtherCurrency(amount, currency);
                out.println("<h4>Le montant converti en "+ currency +" est: "+ result +"</h4>");

                // Mettre le text necessaire dans le message
                message.setText(amount+"#"+email);

                // Obtenir une reference vers une instance de la file de message
                javax.jms.Queue queue =
                        (javax.jms.Queue) jndiContext.lookup("queue/MailContent");

                // Creer un object de type producteur de message dans la file
                MessageProducer messageProducer = sessionQ.createProducer(queue);

                // Envoyer le message
                messageProducer.send(message);

            }


        }

    %>

</body>
</html>
