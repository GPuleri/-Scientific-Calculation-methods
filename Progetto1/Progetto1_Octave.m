clc
path = %percorso

pkg load io
%StocF-1465.mat e Flan_1565.mat NON FUNZIONANO
matrici={'cfd2.mat', 'cfd1.mat', 'G3_circuit.mat', 'parabolic_fem.mat', ...
    'apache2.mat', 'shallow_water1.mat', 'ex15.mat'};
C=cell(length(matrici)+1, 4);
C(1,:)={'Nome' 'Dimensione' 'Tempo' 'Errore'};
for i = 1 : length(matrici)
    %Carico file matrice
    mat = load(strcat(path, matrici{i}));
    pause(1.00); %utile per calcolare la memoria
    
    %CREAZIONE MATRICE A, VETTORE XE, VETTORE B
    A = mat.Problem.A;
    xe = ones(length(A), 1);
    b = A * xe;

    %RISOLUZIONE SISTEMA E CALCOLO TEMPO RISOLUZIONE
    tic;
    x = A \ b;
    time = toc;

    %ERRORE RELATIVO E MEMORIA OCCUPATA
    errore_relativo = norm(x - xe)/norm(xe);

    %RIEMPIMENTO MATRICE RISULTATI
    C(i+1,:)={matrici{i} strcat(num2str(size(A,1)),'x',num2str(size(A,1))) ...
        num2str(time) num2str(errore_relativo)};
end
%CREAZIONE FILE CSV
cell2csv('octaveResultLinux.csv',C);
disp('Fine elaborazione');