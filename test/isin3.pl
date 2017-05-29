use Algorithm::CheckDigits; 
my $isin = CheckDigits('ISIN'); 
while (<DATA>) {
    s[\s+][];
    print "$_: ", $isin->checkdigit($_), " ? ", $isin->is_valid($_), "\n";
}
__DATA__
US0220951033
DE0005066203
TRAAKGRT9105
DE0005201636
AT0000767554
