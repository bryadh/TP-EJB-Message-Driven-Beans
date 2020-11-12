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

                // Demander au MDB de déclancher la demande de conversion
                double amount  = Double.parseDouble(request.getParameter("montant"));
                String currency = request.getParameter("monnaie");
                ConverterBean cb = new ConverterBean();
                double result = cb.euroToOtherCurrency(amount, currency);
                out.println("<h4>Le montant converti en "+ currency +" est: "+ result +"</h4>");


            }


        }

    %>

</body>
</html>
