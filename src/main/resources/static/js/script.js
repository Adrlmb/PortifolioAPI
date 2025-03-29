document.getElementById("usuarioForm").addEventListener("submit", function(event) {
    event.preventDefault(); // Evita recarregar a página

    let purchasedCryptoCode = document.getElementById("purchasedCryptoCode").value;
    let amountCryptoPurchased = document.getElementById("amountCryptoPurchased").value;
    let cryptoUsed = document.getElementById("cryptoUsed").value;
    let amountSpent = document.getElementById("amountSpent").value;
    let taxCryptoCode = document.getElementById("taxCryptoCode").value;
    let taxAmount = document.getElementById("taxAmount").value;
    let buyDate = document.getElementById("buyDate").value;
    let exchange = document.getElementById("exchange").value;

    fetch("http://localhost:8080/buy", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            purchasedCryptoCode:purchasedCryptoCode,
            amountCryptoPurchased: amountCryptoPurchased,
            cryptoUsed:cryptoUsed,
            amountSpent: amountSpent,
            taxCryptoCode: taxCryptoCode,
            taxAmount: taxAmount,
            buyDate: buyDate,
            exchange: exchange
    })

    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Erro HTTP! Status: ${response.status}`);
        }
        return response.json().catch(() => {
            // Fallback se não for JSON
            return { erro: "Resposta inválida do servidor" };
        });
    })
    .then(dados => {
        console.log("Sucesso:", dados);
        carregarProdutos();
    })
    .catch(erro => {
        console.error("Erro:", erro);
    });

});


async function carregarProdutos() {
    try {
        const resposta = await fetch("http://localhost:8080/buy");
        const buy = await resposta.json();
        console.log(buy)

        const tabelaCorpo = document.getElementById("tabela-corpo");
        tabelaCorpo.innerHTML = ""; // Limpa a tabela antes de adicionar novos dados

        buy.forEach(buyDto => {
            // Criando um novo elemento
            const linha = document.createElement("tr");
            // Definindo o conteúdo da linha
            linha.innerHTML = `

                    <td>${buyDto.purchasedCryptoCode}</td>
                    <td>${buyDto.amountCryptoPurchased}
                    <td>${buyDto.cryptoUsed}</td>
                    <td>${buyDto.amountSpent}</td>
                    <td>${buyDto.taxCryptoCode}</td>
                    <td>${buyDto.taxAmount}</td>
                    <td>${buyDto.buyDate}</td>
                    <td>${buyDto.exchange}</td>
                    <td>${buyDto.amountSpent}</td>
                    <td>
                        <button class="btn btn-danger btn-sm" onclick="removerLinha(${buyDto.id}, this)">Excluir</button>
                    </td>
            `;
            tabelaCorpo.appendChild(linha); //adiciona o novo elemento na linha, sem recriar os existentes.
        });
    } catch (erro) {
        console.error("Erro ao carregar produtos:", erro);
    }
}

function removerLinha(id, botao){
    if(confirm("Tem certeza que deseja excluir este item?")){
        fetch(`http://localhost:8080/buy/${id}`,{
            method: "DELETE"
        })
        .then(response => {
            if(!response.ok) {
                throw new Error(`Erro ao excluir! Status: ${response.status}`);
            }
            return response.text();
        })
        .then(() => {
            botao.closest("tr").remove();
        })
        .catch(erro => {
            console.error("Erro ao excluir:", erro);
        })
    }
}
