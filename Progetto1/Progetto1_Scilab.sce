clear
path = //percorso

//StocF-1465.mat e Flan_1565.mat NON FUNZIONANO
matrici=list("cfd2.mtx", "cfd1.mtx", "G3_circuit.mtx", "parabolic_fem.mtx", ...
    "apache2.mtx", "shallow_water1.mtx");
c=cell((size(matrici)+1, 4));
c(1,:)={"Nome,", "Dimensione,", "Tempo,", "Errore"};

for i = 1 : size(matrici);
    //Carico file matrice
    mmread(path+ matrici(i));
    sleep(1000); //utile per calcolare la memoria

    //CREAZIONE MATRICE A, VETTORE B, VETTORE XE
    A = sparse(ans);
    xe = ones(size(A, "r"), 1);
    b = A * xe ;

    //RISOLUZIONE SISTEMA E CALCOLO TEMPO RISOLUZIONE
    tic();
    //[L,P] = spchol(A); ALTERNATIVA MOLTO PIU LENTA
    //x = P*(L'\(L\(P'*b)));
    Cp = taucs_chfact(A);
    x = taucs_chsolve(Cp,b);
    time = toc();

    //ERRORE RELATIVO E MEMORIA OCCUPATA
    errore_relativo = norm(x - xe)/norm(xe);
    
    c(i+1,:) = {matrici(i)+",", string(size(A,1))+"x"+string(size(A,1))+",", string(time)+",", string(errore_relativo)};
end

mat=cell2mat(c);
csvWrite(mat, "scilabResultLinux.csv");
disp('Fine elaborazione');
